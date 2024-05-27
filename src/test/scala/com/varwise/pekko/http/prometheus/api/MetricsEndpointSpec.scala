package com.varwise.pekko.http.prometheus.api

import com.varwise.pekko.http.prometheus.Utils._
import io.prometheus.metrics.core.metrics.Histogram
import io.prometheus.metrics.expositionformats.ExpositionFormats
import io.prometheus.metrics.model.registry.PrometheusRegistry
import org.apache.pekko.http.scaladsl.model.HttpCharsets
import org.apache.pekko.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets
import scala.util.Random

class MetricsEndpointSpec extends AnyFlatSpec with Matchers with ScalatestRouteTest {

  private def createEndpoint(collectorRegistry: PrometheusRegistry) =
    new MetricsEndpoint(collectorRegistry)

  "Metrics endpoint" should "return the correct media type and charset" in {
    val api = createEndpoint(PrometheusRegistry.defaultRegistry)
    Get("/metrics") ~> api.routes ~> check {
      mediaType.subType shouldBe "plain"
      mediaType.isText shouldBe true
      mediaType.params shouldBe Map("version" -> "0.0.4")
      charset shouldBe HttpCharsets.`UTF-8`
    }
  }

  it should "return serialized metrics in the prometheus text format" in {
    val registry = new PrometheusRegistry()
    val api = createEndpoint(registry)
    val RandomTestName = generateRandomStringOfLength(16)
    val RandomTestHelp = generateRandomStringOfLength(16)
    val hist = Histogram.builder().name(RandomTestName).help(RandomTestHelp).register(registry)

    hist.observe(Math.abs(Random.nextDouble()))

    Get("/metrics") ~> api.routes ~> check {
      val resp = responseAs[String]
      val baos = new ByteArrayOutputStream()
      ExpositionFormats.init().getPrometheusTextFormatWriter().write(baos, registry.scrape())
      val expected = new String(baos.toByteArray, StandardCharsets.UTF_8)
      baos.close()
      resp shouldBe expected
    }
  }
}
