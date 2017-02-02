package org.net.cisticola.web;

import io.vertx.ext.web.RoutingContext;

public interface RoutingHandler {
  
  public void info(RoutingContext routingContext);
  public void view(RoutingContext routingContext);
  
}
