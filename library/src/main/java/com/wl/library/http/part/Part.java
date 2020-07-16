package com.wl.library.http.part;




import com.wl.library.utils.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static com.wl.library.http.part.MultipartBody.END_LINE;


/**
 * author:wanglin
 * date:2020/7/7 6:37 PM
 * description:NetworkDemo
 */
public abstract class Part {

    private Part() {
    }

    public abstract String contentType();

    public abstract String heads();

    public abstract void write(OutputStream outputStream) throws IOException;

    /**
     * 创建构建form的part
     *
     * @param key
     * @param value
     * @return
     */
    public static Part create(final String key, final String value) {
        return new Part() {
            @Override
            public String contentType() {
                return null;
            }

            @Override
            public String heads() {
                return Util.trans2FormHead(key);
            }

            @Override
            public void write(OutputStream outputStream) throws IOException {
                outputStream.write(heads().getBytes("UTF-8"));
                outputStream.write(END_LINE);
                outputStream.write(value.getBytes("utf-8"));
                outputStream.write(END_LINE);
            }
        };
    }

    public static Part create(final String type, final String key, final File file) {
        if (file == null) throw new NullPointerException("file 为空");
        if (!file.exists()) throw new IllegalStateException("file 不存在");

        return new Part() {
            @Override
            public String contentType() {
                return type;
            }

            @Override
            public String heads() {
                return Util.trans2FileHead(key, file.getName());
            }

            @Override
            public void write(OutputStream outputStream) throws IOException {
                outputStream.write(heads().getBytes());
                outputStream.write("Content-Type: ".getBytes());
                outputStream.write(Util.getUTF8Bytes(contentType()));
                outputStream.write(END_LINE);
                outputStream.write(END_LINE);
                writeFile(outputStream, file);
                outputStream.write(END_LINE);
                outputStream.flush();
            }

            /**
             * 写出文件
             * @param outputStream 输出流
             * @param file 文件
             * @throws IOException 异常
             */
            private void writeFile(OutputStream outputStream, File file) throws IOException {
                FileInputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(file);
                    int length;
                    byte[] bytes = new byte[2048];
                    while ((length = inputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, length);
                    }
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
            }
        };
    }

}
