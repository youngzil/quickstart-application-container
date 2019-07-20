package org.quickstart.jetty.sample;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ServletContextServer {

  public static void main(String[] args) throws Exception {

    //首先创建一个ServletContextServer类，用来初始化web应用程序的Context，并且指定Servlet和Servlet匹配的url。这里指定了两个Servlet，分别是HelloServlet和GoodbyeServlet，并分别对应/hello/*和/goodbye/*。

    Server server = new Server(8080);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    server.setHandler(context);

    // http://localhost:8080/hello
    context.addServlet(new ServletHolder(new HelloServlet2()), "/hello");
    // http://localhost:8080/hello/kongxx
    context.addServlet(new ServletHolder(new HelloServlet2("Hello Kongxx!")), "/hello/kongxx");

    // http://localhost:8080/goodbye
    context.addServlet(new ServletHolder(new GoodbyeServlet()), "/goodbye");
    // http://localhost:8080/goodbye/kongxx
    context.addServlet(new ServletHolder(new GoodbyeServlet("Goodbye kongxx!")), "/goodbye/kongxx");

    server.start();
    server.join();
  }
}
