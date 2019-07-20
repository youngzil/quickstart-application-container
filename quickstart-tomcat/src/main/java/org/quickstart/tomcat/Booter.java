/**
 * 项目名称：quickstart-container-tomcat 
 * 文件名：Booter.java
 * 版本信息：
 * 日期：2018年4月25日
 * Copyright yangzl Corporation 2018
 * 版权所有 *
 */
package org.quickstart.tomcat;

import java.io.File;
import java.util.Optional;

import javax.servlet.ServletException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;

/**
 * Booter
 * 
 * https://blog.csdn.net/u012550460/article/details/49864931
 * 
 * @author：youngzil@163.com
 * @2018年4月25日 下午12:17:43
 * @since 1.0
 */
public class Booter {
    
    private static final String CONTEXT_PATH = "/javatool-server";

    private static Tomcat tomcat;

    public static Tomcat getTomcat() {
        return tomcat;
    }

    private static StandardServer server;

    private static AprLifecycleListener listener;

    public static void main(String[] args) throws LifecycleException, ServletException {

     // 设定 profile
        Optional<String> profile = Optional.ofNullable(System.getProperty("spring.profiles.active"));
        System.setProperty("spring.profiles.active", profile.orElse("develop"));
        
        tomcat = new Tomcat();
        // 主机名，或ip
        tomcat.setHostname("localhost");
        // 设置端口，80为默认端口
        tomcat.setPort(8080);
        // tomcat用于存储自身的信息，可以随意指定，最好包含在项目目录下
        tomcat.setBaseDir(".");
        // 建立server参照tomcat文件结构
        server = (StandardServer) tomcat.getServer();
        listener = new AprLifecycleListener();
        server.addLifecycleListener(listener);
        // 将appBase设为本项目所在目录
        // tomcat.getHost().setAppBase(".");
        tomcat.getHost().setAppBase(System.getProperty("user.dir") + File.separator + ".");

        // 第二个参数对应docBase为web应用路径，目录下应有WEB-INF,WEB-INF下要有web.xml
        tomcat.addWebapp(CONTEXT_PATH, getAbsolutePath() + "src/main/webapp");
        // 启动tomcat
        tomcat.start();
        // 维持
        tomcat.getServer().await();
    }
    
    private static String getAbsolutePath() {
        String path = null;
        String folderPath = Booter.class.getProtectionDomain().getCodeSource().getLocation().getPath()
                .substring(1);
        if (folderPath.indexOf("target") > 0) {
            path = folderPath.substring(0, folderPath.indexOf("target"));
        }
        return path;
    }

}
