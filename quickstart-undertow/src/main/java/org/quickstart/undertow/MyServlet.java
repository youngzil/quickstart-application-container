package org.quickstart.undertow;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/11/23 23:02
 */
public class MyServlet extends HttpServlet {

  private static final long serialVersionUID = 2378494112650465478L;

  private static final String message = "This is MyServlet";

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doPost(req, resp);
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    writer.write(message);
    writer.close();
  }

}
