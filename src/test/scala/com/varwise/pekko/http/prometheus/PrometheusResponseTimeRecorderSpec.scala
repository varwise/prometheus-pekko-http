package com.varwise.pekko.http.prometheus

import com.varwise.pekko.http.prometheus.Utils._
import io.prometheus.client.{Collector, CollectorRegistry}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration
import scala.concurrent.duration.FiniteDuration
import scala.util.Random

class PrometheusResponseTimeRecorderSpec extends AnyFlatSpec with Matchers {

  private def getBucketValue(
      registry: CollectorRegistry,
      metricName: String,
      labelNames: List[String],
      labelValues: List[String],
      bucket: Double
  ): Int = {
    val name = metricName + "_bucket"

    // 'le' should be the first label in the list
    val allLabelNames = (Array("le") ++ labelNames).reverse
    val allLabelValues = (Array(Collector.doubleToGoString(bucket)) ++ labelValues).reverse
    registry.getSampleValue(name, allLabelNames, allLabelValues).intValue()
  }

  "PrometheusLatencyRecorder" should "register a histogram and record request latencies" in {
    val registry = new CollectorRegistry()
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

    val first =
      getBucketValue(registry, randomMetricName, List(randomLabelName), List(randomEndpointName), buckets.head)
    val second =
      getBucketValue(registry, randomMetricName, List(randomLabelName), List(randomEndpointName), buckets.last)
    val positiveInf = getBucketValue(
      registry,
      randomMetricName,
      List(randomLabelName),
      List(randomEndpointName),
      Double.PositiveInfinity
    )

    first shouldBe 0
    second shouldBe 1
    positiveInf shouldBe 1
  }
}
