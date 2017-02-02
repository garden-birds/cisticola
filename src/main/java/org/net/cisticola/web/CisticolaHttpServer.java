package org.net.cisticola.web;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.Vertx;

public class CisticolaHttpServer {
  private static InternalLogger log = Log4JLoggerFactory.getInstance("cisticola");
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new CisticolaVerticle(), stringAsyncResult -> {
      log.info("cisticola verticle has been deployed successfully!");
    });
  }
}
