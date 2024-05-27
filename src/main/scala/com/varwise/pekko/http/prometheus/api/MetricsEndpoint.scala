package com.varwise.pekko.http.prometheus.api

import io.prometheus.metrics.expositionformats.ExpositionFormats
import io.prometheus.metrics.model.registry.PrometheusRegistry
import org.apache.pekko.http.scaladsl.model.{HttpCharsets, HttpEntity, MediaType}
import org.apache.pekko.http.scaladsl.server.Directives._
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko.stream.scaladsl.StreamConverters
import org.slf4j.LoggerFactory

import java.io.{PipedInputStream, PipedOutputStream}
import scala.concurrent.{ExecutionContext, Future}

class MetricsEndpoint(registry: PrometheusRegistry)(implicit ec: ExecutionContext) {

  private val logger = LoggerFactory.getLogger(classOf[MetricsEndpoint])
  private val prometheusTextType =
    MediaType.customWithFixedCharset("text", "plain", HttpCharsets.`UTF-8`, params = Map("version" -> "0.0.4"))
  private val writer = ExpositionFormats.init().getPrometheusTextFormatWriter()

  val routes: Route =
    get {
      path("metrics") {
        complete {
          val in = new PipedInputStream
          val out = new PipedOutputStream(in)
          val byteSource = StreamConverters.fromInputStream(() => in)
          val f = Future {
            try
              writer.write(out, registry.scrape())
            finally
              out.close()
          }
          f.failed.foreach(e => logger.error("Error when writing Prometheus metrics", e))
          HttpEntity(prometheusTextType, byteSource)
        }
      }
    }

}
