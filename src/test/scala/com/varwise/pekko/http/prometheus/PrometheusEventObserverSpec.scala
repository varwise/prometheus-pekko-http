package com.varwise.pekko.http.prometheus

import com.varwise.pekko.http.prometheus.Utils._
import io.prometheus.client.CollectorRegistry
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PrometheusEventObserverSpec extends AnyFlatSpec with Matchers {

  "PrometheusEventObserver" should "record observed events in a counter" in {
    val registry = new CollectorRegistry()
    val randomMetricName = generateRandomString
    val randomMetricHelp = generateRandomString
    val randomEventLabelName = generateRandomString
    val randomEventDetailsLabelName = generateRandomString

    val randomEventName = generateRandomString
    val randomEventDetails = generateRandomString

    val eventObserver = new PrometheusEventObserver(
      randomMetricName,
      randomMetricHelp,
      randomEventLabelName,
      randomEventDetailsLabelName,
      registry
    )

    def getCounterValue: java.lang.Double =
      registry.getSampleValue(
        randomMetricName + "_total",
        Array(randomEventLabelName, randomEventDetailsLabelName),
        Array(randomEventName, randomEventDetails)
      )

    getCounterValue shouldBe null

    eventObserver.observe(randomEventName, randomEventDetails)

    getCounterValue should not be null
    getCounterValue.intValue() shouldBe 1

  }
}
