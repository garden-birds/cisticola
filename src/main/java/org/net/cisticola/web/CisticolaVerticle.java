package org.net.cisticola.web;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.ArrayList;
import java.util.List;

import org.net.cisticola.util.CisticolaResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CisticolaVerticle extends AbstractVerticle implements RoutingHandler {
  private static InternalLogger log = Log4JLoggerFactory.getInstance("cisticola");
  
  private Router router;

  @Override
  public JsonObject config() {
    return super.config();
  }

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    super.start(startFuture);
    router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    router.get("/cisticola/api/v1/").handler(this::info);
    router.get("/cisticola/api/v1/view").handler(this::view);

    vertx.createHttpServer().requestHandler(router::accept).listen(8989);
  }

  @Override
  public void stop(Future<Void> stopFuture) throws Exception {
    super.stop(stopFuture);
  }

  @Override
  public void info(RoutingContext routingContext) {
    List<String> availableRoutes = new ArrayList<String>();

    List<Route> allRoutes = router.getRoutes();
    for (Route r : allRoutes) {
      if (r.getPath() != null && !(r.getPath().isEmpty())) {
        availableRoutes.add(r.getPath());
      }
    }
    log.debug("allRoutes: " + availableRoutes.size());
    CisticolaResponse<List<String>> r = new CisticolaResponse<List<String>>();
    r.setStatus(200);
    r.setTitle("ListRoutes");
    r.setResult(availableRoutes);
    Buffer responseData = toBuffer(r);
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers().add("Content-Length", responseData.length() + "")
        .add("Content-Type", "application/json");

    response.end(responseData);
  }

  @Override
  public void view(RoutingContext routingContext) {    
    CisticolaResponse<List<String>> r = new CisticolaResponse<List<String>>();
    r.setStatus(200);
    r.setTitle("CisticolaView");

    List<String> responseView = new ArrayList<String>();
    responseView.add("Welcome to cisticola web view!");
    r.setResult(responseView);
    Buffer responseData = toBuffer(r);
    HttpServerResponse response = routingContext.response();
    response.setStatusCode(200);
    response.headers().add("Content-Length", responseData.length() + "")
        .add("Content-Type", "application/json");

    response.end(responseData);
  }

  private Buffer toBuffer(CisticolaResponse result) {
    Buffer responseData = null;
    try {
      String jsonString = new ObjectMapper().writeValueAsString(result);
      responseData = Buffer.buffer(jsonString.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return responseData;
  }
}
