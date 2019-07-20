/**
 * 项目名称：quickstart-container-jetty 
 * 文件名：HelloServlet.java
 * 版本信息：
 * 日期：2018年4月25日
 * Copyright yangzl Corporation 2018
 * 版权所有 *
 */
package org.quickstart.tomcat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HelloServlet
 * 
 * @author：youngzil@163.com
 * @2018年4月25日 上午11:00:57
 * @since 1.0
 */
public class HelloServlet extends HttpServlet {

    private static final long serialVersionUID = 5639776356541557752L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("<h1 style='color:red'>Hello,I am embeded tomcat!</h1>\r\n");
    }

}
