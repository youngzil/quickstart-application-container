package org.quickstart.smart.http;

import org.smartboot.http.server.HttpBootstrap;
import org.smartboot.http.server.HttpRequest;
import org.smartboot.http.server.HttpResponse;
import org.smartboot.http.server.HttpServerHandle;
import org.smartboot.http.server.WebSocketRequest;
import org.smartboot.http.server.WebSocketResponse;
import org.smartboot.http.server.handle.WebSocketDefaultHandle;

import java.io.IOException;

public class SimpleSmartHttpServer {

    // 浏览器访问:http://localhost:8080/，亦或采用websocket请求ws://127.0.0.1:8080/

    public static void main(String[] args) {
        HttpBootstrap bootstrap = new HttpBootstrap();
        // 普通http请求
        bootstrap.pipeline().next(new HttpServerHandle() {
            @Override
            public void doHandle(HttpRequest request, HttpResponse response) throws IOException {
                response.write("hello world<br/>".getBytes());
            }
        });
        // websocket请求
        bootstrap.wsPipeline().next(new WebSocketDefaultHandle() {
            @Override
            public void handleTextMessage(WebSocketRequest request, WebSocketResponse response, String data) {
                response.sendTextMessage("Hello World");
            }
        });
        bootstrap.setPort(8080).start();
    }
}
