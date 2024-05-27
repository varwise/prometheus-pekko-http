package com.varwise.pekko.http.prometheus

import com.varwise.pekko.http.prometheus.Utils._
import io.prometheus.metrics.model.registry.PrometheusRegistry
import io.prometheus.metrics.model.snapshots.{CounterSnapshot, Labels}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.jdk.CollectionConverters.IterableHasAsScala

class PrometheusEventObserverSpec extends AnyFlatSpec with Matchers {

  "PrometheusEventObserver" should "record observed events in a counter" in {
    val registry = new PrometheusRegistry()
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

    def getCounterValue(): Option[Double] = {
      val labels = Labels.of(
        randomEventLabelName,
        randomEventName,
        randomEventDetailsLabelName,
        randomEventDetails
      )
      registry
        .scrape()
        .asScala
        .collect { case counter: CounterSnapshot => counter }
        .filter(_.getMetadata.getName == randomMetricName)
        .flatMap(_.getDataPoints.asScala)
        .filter(_.getLabels.equals(labels))
        .map(_.getValue)
        .headOption
    }

    getCounterValue() shouldBe None

    eventObserver.observe(randomEventName, randomEventDetails)

    getCounterValue() should not be None
    getCounterValue().get.intValue() shouldBe 1
  }
}
