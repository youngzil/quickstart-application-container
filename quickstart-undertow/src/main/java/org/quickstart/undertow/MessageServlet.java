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
 * @createTime 2019/11/23 23:01
 */
public class MessageServlet extends HttpServlet {

  private static final long serialVersionUID = 6861632231065498153L;

  private static final String message = "This is MessageServlet";

  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doPost(req, resp);
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    writer.write(message);
    writer.close();
  }

}
