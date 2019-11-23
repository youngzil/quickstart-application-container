package org.quickstart.undertow;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import javax.servlet.ServletException;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/11/23 23:02
 */
public class ServletServer {

  // http://localhost:8080/myapp/messageServlet
  // http://localhost:8080/myapp/myServlet

  public static final String MYAPP = "/myapp";

  public static void main(String[] args) throws ServletException {

    // 创建包部署对象，包含多个servletInfo。可以认为是servletInfo的集合
    DeploymentInfo deploymentInfo = Servlets.deployment()//
        // 指定ClassLoader
        .setClassLoader(ServletServer.class.getClassLoader())//
        // 应用上下文(必须与映射路径一致，否则sessionId会出现问题，每次都会新建)
        .setContextPath(MYAPP)
        // 设置部署包名
        .setDeploymentName("myapp.war")
        // 添加servletInfo到部署对象中
        .addServlets(
            // 创建ServletInfo，Servelt的最小单位。是对javax.servlet.Servlet具体实现的再次封装。注意：ServletInfo的name必须是唯一的
            Servlets.servlet("MessageServlet", MessageServlet.class)//
                // 绑定映射为/myServlet
                .addMappings("/messageServlet")//
                // 创建servletInfo的初始化参数
                .addInitParam("message", "This is my first MyServlet!"),//
            Servlets.servlet("MyServlet", MyServlet.class)//
                .addMappings("/myServlet"));

    // 使用默认的servlet容器，并将部署添加至容器,容器是用来管理DeploymentInfo，一个容器可以添加多个DeploymentInfo
    // 将部署添加至容器并生成对应的容器管理对象，包部署管理。是对添加到ServletContaint中DeploymentInfo的一个引用，用于运行发布和启动容器
    DeploymentManager manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
    manager.deploy();// 实施部署

    HttpHandler servletHandler = manager.start();//启动容器，生成请求处理器

    // 分发器：将用户请求分发给对应的HttpHandler
    // servlet path处理器，DeploymentManager启动后返回的Servlet处理器。
    PathHandler path = Handlers.path(Handlers.redirect(MYAPP))//
        .addPrefixPath(MYAPP, servletHandler);//绑定映射关系

    Undertow server = Undertow.builder()//
        .addHttpListener(8080, "localhost")//绑定端口号和主机
        .setHandler(path)//设置分发处理器
        .build();
    server.start();//启动server
  }

}
