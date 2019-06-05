package org.quickstart.container.jetty.sample;

import java.net.BindException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.http.HttpParser.RequestHandler;
import org.eclipse.jetty.jmx.ObjectMBean;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.quickstart.container.jetty.example.AsyncEchoServlet;
import org.quickstart.container.jetty.example.HelloServlet;


public class JettyServer {


  public static void main(String[] args) {
    Server server = null;
    try {

      //server可以添加QueuedThreadPool、ServerConnector、HandlerCollection
      //context可以添加Servlet、Filter

      QueuedThreadPool objQueuedThreadPool = new QueuedThreadPool();
      objQueuedThreadPool.setName("RestfulThreadPool[" + "Test" + "]-");
      objQueuedThreadPool.setMinThreads(10);
      objQueuedThreadPool.setMaxThreads(20);

      server = new Server(objQueuedThreadPool);
      server.manage(objQueuedThreadPool);

      ServerConnector connector = new ServerConnector(server);
      connector.setPort(8080);
      connector.setAcceptQueueSize(5000);
      connector.setIdleTimeout(30000);
      server.addConnector(connector);
      //server.setConnectors(new Connector[]{connector});//批量设置ServerConnector

      ServletContextHandler context = new ServletContextHandler(server, "/");

      //ServletContextHandler context = new ServletContextHandler();
      //context.setContextPath("/");
      HandlerCollection handlers = new HandlerCollection();
      handlers.setHandlers(new Handler[]{context, new DefaultHandler()});
      server.setHandler(handlers);

      //设置禁用 put delete trace方法
      Constraint constraint = new Constraint();
      constraint.setName("Disable TRACE,OPTIONS");
      constraint.setAuthenticate(true);

      ConstraintMapping traceMapping = new ConstraintMapping();
      traceMapping.setConstraint(constraint);
      traceMapping.setMethod("TRACE");
      traceMapping.setPathSpec("/*");

      ConstraintMapping optionsMapping = new ConstraintMapping();
      optionsMapping.setConstraint(constraint);
      optionsMapping.setMethod("OPTIONS");
      optionsMapping.setPathSpec("/*");

      List<ConstraintMapping> mappingList = getConstraintMappings();
      Set<String> knownRoles = new HashSet<>();
      knownRoles.add("user");
      knownRoles.add("administrator");

      ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();

      securityHandler.addConstraintMapping(traceMapping);
      securityHandler.addConstraintMapping(optionsMapping);
      securityHandler.setConstraintMappings(mappingList, knownRoles);
      
      context.setSecurityHandler(securityHandler);

      //设置Servlet
      ServletHolder helloServlet = context.addServlet(HelloServlet.class, "/hello");
      Map paramMap = new HashMap();
      paramMap.put("javax.ws.rs.Application",
          "com.yangzl.openplatform.isb.restful.server.servlet.AopResourceConfig");
      helloServlet.setInitParameters(paramMap);
      helloServlet.setInitOrder(1);

      context.addServlet(AsyncEchoServlet.class, "/echo/*");
      //context.addServlet(new ServletHolder(servlet), "/*");

      FilterHolder filterHolder = context.addFilter(TryFilesFilter.class, "/*", EnumSet.of(
          DispatcherType.REQUEST));
      filterHolder.setName("TryFilesFilter");
      filterHolder
          .setInitParameter(TryFilesFilter.FILES_INIT_PARAM, "$path " + "s" + "?p=$path");

      //context.addEventListener();

      server.setStopAtShutdown(true);// kill 进程时执行钩子清除连接资源
      server.start();
      server.join();


    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    } finally {
      if (server != null && server.isFailed()) {
        try {
          server.stop();
          server.join();
        } catch (Exception e) {
          System.out.println(e.getStackTrace());
        }

      }
    }

  }

  private static List<ConstraintMapping> getConstraintMappings() {
    Constraint constraint0 = new Constraint();
    constraint0.setAuthenticate(true);
    constraint0.setName("forbid");
    ConstraintMapping mapping0 = new ConstraintMapping();
    mapping0.setPathSpec("/forbid/*");
    mapping0.setConstraint(constraint0);

    Constraint constraint1 = new Constraint();
    constraint1.setAuthenticate(true);
    constraint1.setName("auth");
    constraint1.setRoles(new String[]{Constraint.ANY_ROLE});
    ConstraintMapping mapping1 = new ConstraintMapping();
    mapping1.setPathSpec("/auth/*");
    mapping1.setConstraint(constraint1);

    Constraint constraint2 = new Constraint();
    constraint2.setAuthenticate(true);
    constraint2.setName("admin");
    constraint2.setRoles(new String[]{"administrator"});
    ConstraintMapping mapping2 = new ConstraintMapping();
    mapping2.setPathSpec("/admin/*");
    mapping2.setConstraint(constraint2);
    mapping2.setMethod("GET");

    Constraint constraint3 = new Constraint();
    constraint3.setAuthenticate(false);
    constraint3.setName("relax");
    ConstraintMapping mapping3 = new ConstraintMapping();
    mapping3.setPathSpec("/admin/relax/*");
    mapping3.setConstraint(constraint3);

    Constraint constraint4 = new Constraint();
    constraint4.setAuthenticate(true);
    constraint4.setName("loginpage");
    constraint4.setRoles(new String[]{"administrator"});
    ConstraintMapping mapping4 = new ConstraintMapping();
    mapping4.setPathSpec("/testLoginPage");
    mapping4.setConstraint(constraint4);

    Constraint constraint5 = new Constraint();
    constraint5.setAuthenticate(false);
    constraint5.setName("allow forbidden POST");
    ConstraintMapping mapping5 = new ConstraintMapping();
    mapping5.setPathSpec("/forbid/post");
    mapping5.setConstraint(constraint5);
    mapping5.setMethod("POST");

    Constraint constraint6 = new Constraint();
    constraint6.setAuthenticate(false);
    constraint6.setName("data constraint");
    constraint6.setDataConstraint(2);
    ConstraintMapping mapping6 = new ConstraintMapping();
    mapping6.setPathSpec("/data/*");
    mapping6.setConstraint(constraint6);

    Constraint constraint7 = new Constraint();
    constraint7.setAuthenticate(true);
    constraint7.setName("** constraint");
    constraint7.setRoles(new String[]{Constraint.ANY_AUTH,
        "user"}); //the "user" role is superfluous once ** has been defined
    ConstraintMapping mapping7 = new ConstraintMapping();
    mapping7.setPathSpec("/starstar/*");
    mapping7.setConstraint(constraint7);

    return Arrays
        .asList(mapping0, mapping1, mapping2, mapping3, mapping4, mapping5, mapping6, mapping7);
  }


}
