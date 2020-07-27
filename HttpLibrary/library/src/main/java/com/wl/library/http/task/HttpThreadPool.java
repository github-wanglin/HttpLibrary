package com.wl.library.http.task;



import com.wl.library.http.response.Response;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * author:wanglin
 * date:2020/7/6 6:42 PM
 * description:线程池任务
 */
public class HttpThreadPool {

    /**
     * 线程核心数
     */
    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * 最大存活时间
     */
    public static final int LIVE_TIME = 10;

    /**
     * 单例对象
     */
    private static volatile HttpThreadPool threadPool;

    /**
     * 无界队列
     */
    private BlockingQueue<Future<?>> queue = new LinkedBlockingDeque<>();

    /**
     * 线程池
     */
    private ThreadPoolExecutor executor;

    public static HttpThreadPool getInstance() {
        if (threadPool == null) {
            synchronized (HttpThreadPool.class) {
                if (threadPool == null) {
                    threadPool = new HttpThreadPool();
                }
            }
        }
        return threadPool;
    }

    private HttpThreadPool() {
        executor = new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_SIZE + 1, LIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<>(4), rejectHandler);
        executor.execute(runnable);
    }

    /**
     * 消费者
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                FutureTask<?> task = null;
                try {
                    task = (FutureTask<?>) queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (task != null) {
                    executor.execute(task);
                }
            }
        }
    };

    /**
     * @param task 任务
     * @return response对象
     * @throws ExecutionException   异常
     * @throws InterruptedException 异常
     */
    public synchronized Response submit(Callable<Response> task) throws ExecutionException, InterruptedException {
        if (task == null) throw new NullPointerException("task == null,无法执行");
        Future<Response> future = executor.submit(task);
        return future.get();
    }

    /**
     * 添加异步任务
     *
     * @param task 任务
     */
    public void execute(FutureTask<?> task) {
        if (task == null) throw new NullPointerException("task == null,无法执行");
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    RejectedExecutionHandler rejectHandler = new RejectedExecutionHandler() {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                queue.put(new FutureTask<>(runnable, null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
