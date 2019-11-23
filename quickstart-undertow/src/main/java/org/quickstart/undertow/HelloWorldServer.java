package org.quickstart.undertow;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * @author youngzil@163.com
 * @description TODO
 * @createTime 2019/11/23 22:58
 */
public class HelloWorldServer {

  // http://localhost:8080/

  public static void main(final String[] args) {
    Undertow server = Undertow.builder()
        .addHttpListener(8080, "localhost")
        .setHandler(new HttpHandler() {
          @Override
          public void handleRequest(final HttpServerExchange exchange) throws Exception {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send("Hello World");
          }
        }).build();
    server.start();
  }

}

