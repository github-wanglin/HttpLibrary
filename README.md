## TCP使用：支持传输Object对象

#### 新建服务端：
*端口号：1080*
*return 服务器返回给客户端的object*
```Server bioServer = ServerFactory.getServer(1080, new BiFunction<Socket, byte[], Object>() {
          @Override
          public Object apply(Socket socket, byte[] bytes) {
             return null;
          }
});
bioServer.start();
```
#### 新建客户端：
*IP地址：192.168.31.137*
*端口号：1080*
```Client client = ClientFactory.getClient("192.168.31.137", 1080, new TcpConnectCallback() {
       @Override
       public void connected() {

       }

       @Override
       public void disConnected() {

       }
});
```
* 发送数据:
```
<byte[] result  = client.send(obiect);>
```
* 断开链接：
```
try {
   if (client != null && client.getSocket() != null) {
     client.getSocket().close();
     client.close();
   }
} catch (IOException e) {
   e.printStackTrace();
}
```
## HTTP使用：支持get、post、put、delete请求方式，支持同步和异步的方式构建请求

#### 同步请求：

```
Request request = new Request.Builder()
          .url("https://www.wanandroid.com/article/list/0/json")
          .build();

Response response = client.newCall(request).execute();

if (response != null && response.code() == 200) {
  data.setText(response.body().string());
 }
 ```
#### 异步请求：
```
Request request = new Request.Builder()
          .url("https://www.wanandroid.com/banner/json")
          .build();

client.newCall(request).enqueue(new Callback() {
     @Override
     public void onResponse(Response response) {

         if (response != null && response.code() == 200) {
           data.setText(response.body().string());
           }
      }

     @Override
     public void onFail(Request request, IOException e) {
         data.setText(e.getMessage());
     }
});
```
#### GET方式：
```
Request request = new Request.Builder()
          .url("https://www.wanandroid.com/banner/json")
          .get()
          .build();

Response response = client.newCall(request).execute();

if (response != null && response.code() == 200) {
  data.setText(response.body().string());
 }
```
#### POST方式：
```
FormBody body = new FormBody.Builder()
         .add("username", "张三")
         .add("pwd", "abc123")
         .build();

Request request = new Request.Builder()
        .url("http://192.168.31.34:8080/API/upkeep")
        .post(body)
        .build();

Response response = client.newCall(request).execute();

if (response != null && response.code() == 200) {
  data.setText(response.body().string());
 }
```
####参数+文件上传：
```
String path = Environment.getExternalStorageDirectory().getAbsolutePath();

File file = new File(path + "/tempCropped.jpeg");
File file1 = new File(path + "/head_image.png");
File file2 = new File(path + "/shumei.txt");
File file3 = new File(path + "/IMG_0358.MP4");

MultipartBody body = new MultipartBody.Builder()
           .addForm("username", "123")
           .addPart("image/jpeg", "temp.jpg", file)
           .addPart("image/png", "head_image.png", file1)
           .addPart("text/plain", "1.txt", file2)
           .addPart("video/mpeg4", "2.mp4", file3)
           .build();

Request request = new Request.Builder()
           .url("http://192.168.31.34:8080/API/upload")
           .post(body)
           .build();

Response response = client.newCall(request).execute();

if (response != null && response.code() == 200) {
  data.setText(response.body().string());
 }
 ```
