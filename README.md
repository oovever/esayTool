# esayTool
常见工具封装

### HttpUtil

* 基于HttpClient4.3封装

* 请求连接时

* 创建请求

  * HttpUtil.get(URL) 创建get请求
  * HttpUtil.post(URL) 创建post请求
  * HttpUtil.delete(URL) 创建delete请求
  * HttpUtil.Put(URL) 创建delete请求

* 添加参数

  * HttpUtil.post(URL).addPatameter()
  * HttpUtil.post(URL).setPatameter()
  * HttpUtil.post(URL).useForm()

* 上传文件

  * FormPart form = FormPart.create("name", "value"); 
  * form.addParameter("fieldName", new File("test.txt")); 
  * HttpUtil.post(URL).useForm(form); 

* 添加Cookie

  * HttpUtil.post(URL).addCookie(new BasicClientCookie("name", "value")); 

* 添加header

  * HttpUtil.post(URL).addHeader(”header“);

* 设置网络代理

  * HttpUtil.post(URL).setProxy(url, port); 

* 执行请求

    * execute() 使用默认连接池执行请求

    * excuteCallbace 异步多线程方式执行请求 

    * HttpUtil.get(URL).excute().getString()返回执行String结果
### IO

* 拷贝

  * IoUtil.copy(in,out,拷贝大小)
    * reader writer
    * InputStream OutputStream
    * FileInputStream FileOutputStream 
    * NIO Copy IoUtil.copyByNIO(in,out,拷贝大小)

* 转化

  * IoUtil.getReader 将InputStream转化为BufferedReader
  * IoUtil.GetWriter用于将OutputStream转化为OutputStreamWriter
  * String转化为流IoUtil.ByteArrayInputStream(String )
  * File转化为FileInputStream IoUtil.toStream（File file）

* 读取内容

  * IoUtil.read(InputStream/Reader/FileChannel) 读取流中的内容

  * IoUtil.readBytes 返回byte数组

  * IoUtil.readLines 按行读取\

* 写入

  * IoUtil.write(),提供byte,Object内容写入到流中

* 关闭
  * IoUtil.close关闭对象

* 文件操作

  * FileUtil.ls 列出房钱名双手的文件

  * FileUtil.mkdir 创建目录

  * FileUtil.del 递归删除目录

  * FileUtil.copy拷贝文件或者目录

* 文件读取

   ```java
    FileReader fileReader = new FileReader(path);
    byte[] res = fileReader.readBytes();
    String res = fileReader.readString();
    List<String> res= fileReader.readLines();
   ```

* 文件写入

   ```java
   FileWriter writer = new FileWriter(path);
   writer.write("test1");
   writer.write("test2");
   writer.write("test3");
   //        追加写入
   writer.append("test append1");
   //        普通写入
   List<String> list = new ArrayList<>();
   list.add("test1");
   list.add("test2");
   list.add("test3");
   writer.writeLines(list);
   //追加写入
   writer.writeLines(list, true);
   ```

     



  
