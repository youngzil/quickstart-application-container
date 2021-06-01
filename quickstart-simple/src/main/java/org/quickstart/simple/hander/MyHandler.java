package org.quickstart.simple.hander;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class MyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String content = "hello world";
        //设置响应头属性及响应信息的长度
        httpExchange.sendResponseHeaders(200, content.length());
        //获得输出流
        OutputStream os = httpExchange.getResponseBody();
        os.write(content.getBytes());
        os.close();
    }

}