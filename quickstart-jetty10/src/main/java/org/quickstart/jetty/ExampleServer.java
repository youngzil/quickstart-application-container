/**
 * 项目名称：quickstart-container-jetty 文件名：ExampleServer.java 版本信息： 日期：2018年4月25日 Copyright yangzl Corporation 2018 版权所有 *
 */
package org.quickstart.jetty;

import cn.hangzhou.demo.servlet.LegalServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 * ExampleServer
 *
 * @author：youngzil@163.com
 * @2018年4月25日 上午10:58:44
 * @since 1.0
 */
public class ExampleServer {

  public static void main(String[] args) throws Exception {

    QueuedThreadPool threadPool = new QueuedThreadPool(2);
    threadPool.setName("ddd-");
    threadPool.setMaxThreads(10);
    threadPool.setMinThreads(1);
    Server server = new Server(threadPool);
    // Server server = new Server();

    ServerConnector connector = new ServerConnector(server);
    connector.setPort(9001);
    server.setConnectors(new Connector[]{connector});

    ServletContextHandler context = new ServletContextHandler();
    context.setContextPath("/");
    context.addServlet(HelloServlet.class, "/hello");
    context.addServlet(LegalServlet.class, "/oauth2/authorization/checkForZjService");
    context.addServlet(AsyncEchoServlet.class, "/echo/*");
    HandlerCollection handlers = new HandlerCollection();
    handlers.setHandlers(new Handler[]{context, new DefaultHandler()});
    server.setHandler(handlers);

    server.start();
    server.join();

  }

}
