package org.quickstart.simple.server;

import com.sun.net.httpserver.HttpServer;
import org.quickstart.simple.hander.MyHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 根据Java提供的API实现Http服务器
 */
public class SimpleHttpServer {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // 创建HttpServer服务器
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8888), 10);
        //将 / 请求交给MyHandler处理器处理
        httpServer.createContext("/test", new MyHandler());
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(10);
        httpServer.setExecutor(threadPoolExecutor);
        // httpServer.setExecutor(null); // creates a default executor
        httpServer.start();
        System.out.println(" Server started on port 8888");
    }
}
