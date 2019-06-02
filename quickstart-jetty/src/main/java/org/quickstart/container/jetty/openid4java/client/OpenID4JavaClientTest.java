package org.quickstart.container.jetty.openid4java.client;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class OpenID4JavaClientTest {

  public static void main(String[] args) throws Exception {

//使用下面连接访问
    //http://localhost:8080/client/ff

    Server server = new Server();

    ServerConnector connector = new ServerConnector(server);
    connector.setPort(8080);
    connector.setAcceptQueueSize(5000);
    connector.setIdleTimeout(30000);
    server.addConnector(connector);

    ServletContextHandler context = new ServletContextHandler(server, "/");

    HandlerCollection handlers = new HandlerCollection();
    handlers.setHandlers(new Handler[]{context, new DefaultHandler()});
    server.setHandler(handlers);

    //设置Servlet
    context.addServlet(ClientServlet.class, "/client/*");

    server.setStopAtShutdown(true);// kill 进程时执行钩子清除连接资源
    server.start();
    server.join();


  }

}
