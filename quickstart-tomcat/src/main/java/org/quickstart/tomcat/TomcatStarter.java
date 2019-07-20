/**
 * 项目名称：quickstart-container-tomcat 
 * 文件名：TomcatStarter.java
 * 版本信息：
 * 日期：2018年4月25日
 * Copyright yangzl Corporation 2018
 * 版权所有 *
 */
package org.quickstart.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.startup.Tomcat;

/**
 * TomcatStarter
 * 
 * @author：youngzil@163.com
 * @2018年4月25日 下午12:27:40
 * @since 1.0
 */
public class TomcatStarter {

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
//        tomcat.setHostname("localhost");
        tomcat.setPort(8090);
        // tomcat.setBaseDir("/temp");
        // Server server = tomcat.getServer();
        // server.setPort(8080);
        Service service = tomcat.getService();
        service.setName("Tomcat-embbeded");
        // Connector connector = tomcat.getConnector();
        // connector.setPort(8888);
        Context context = tomcat.addContext("/hello", "/");
        Tomcat.addServlet(context, "test", new HelloServlet());
//        context.addServletMapping("/test", "test");
        context.addServletMappingDecoded("/test", "test");
        
        tomcat.init();  
        // 启动tomcat
        tomcat.start();
        // 维持
        tomcat.getServer().await();
    }

}
