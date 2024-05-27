package com.varwise.pekko.http.prometheus

import com.varwise.pekko.http.prometheus.Utils._
import io.prometheus.metrics.model.registry.PrometheusRegistry
import io.prometheus.metrics.model.snapshots.{HistogramSnapshot, Labels}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration
import scala.concurrent.duration.FiniteDuration
import scala.jdk.CollectionConverters.IterableHasAsScala
import scala.util.Random

class PrometheusResponseTimeRecorderSpec extends AnyFlatSpec with Matchers {

  private def getBucketValue(
      registry: PrometheusRegistry,
      metricName: String,
      labels: Labels,
      bucket: Double
  ): Long =
    registry
      .scrape()
      .asScala
      .collect { case metric: HistogramSnapshot => metric }
      .filter(_.getMetadata.getName == metricName)
      .flatMap(_.getDataPoints.asScala)
      .filter(_.getLabels.equals(labels))
      .flatMap(_.getClassicBuckets.asScala)
      .find(_.getUpperBound == bucket)
      .get
      .getCount

  "PrometheusResponseTimeRecorder" should "register a histogram and record request latencies" in {
    val registry = new PrometheusRegistry()
    val randomMetricName = generateRandomString
    val randomMetricHelp = generateRandomString
    val randomLabelName = generateRandomString
    val randomEndpointName = generateRandomString
    val randomLatency = Math.abs(Random.nextInt(10000))

    // our random value will end up in the second bucket
    val buckets = List((randomLatency - 1).toDouble, (randomLatency + 1).toDouble)

    val recorder = new PrometheusResponseTimeRecorder(
      randomMetricName,
      randomMetricHelp,
      buckets,
      randomLabelName,
      registry,
      duration.MILLISECONDS
    )

    recorder.recordResponseTime(randomEndpointName, FiniteDuration(randomLatency, duration.MILLISECONDS))

    val labels = Labels.of(randomLabelName, randomEndpointName)

    val first = getBucketValue(registry, randomMetricName, labels, buckets.head)
    val second = getBucketValue(registry, randomMetricName, labels, buckets.last)
    val positiveInf = getBucketValue(registry, randomMetricName, labels, Double.PositiveInfinity)

    first shouldBe 0
    second shouldBe 1
    positiveInf shouldBe 0
  }
}
