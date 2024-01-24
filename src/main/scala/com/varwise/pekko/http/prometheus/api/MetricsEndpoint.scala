package com.varwise.pekko.http.prometheus.api

import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.server.Route
import io.prometheus.client.CollectorRegistry

class MetricsEndpoint(registry: CollectorRegistry) {

  val routes: Route =
    get {
      path("metrics") {
        complete {
          MetricFamilySamplesEntity.fromRegistry(registry)
        }
      }
    }

}
