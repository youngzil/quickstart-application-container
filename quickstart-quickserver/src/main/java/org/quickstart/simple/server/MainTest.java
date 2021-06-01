package org.quickstart.simple.server;

import java.io.File;
import org.quickserver.net.AppException;
import org.quickserver.net.server.QuickServer;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019-07-20 11:47
 */
public class MainTest {

  public static void main(String[] args) throws AppException {

    final QuickServer server = new QuickServer();

    //pick the xml file form config folder
    // String confFile = "MyServer.xml";
    String confFile = "quickstart-quickserver/src/main/resources/conf" + File.separator + "EchoServer.xml";
    Object config[] = new Object[]{confFile};
    if (server.initService(config) != true) {
      System.err.println("Could't init server !!");
    }

    QuickServer.load(confFile).stopServer();

    // server.startServer();

    // server.stopService();
    // server.stopServer();

  }

}
