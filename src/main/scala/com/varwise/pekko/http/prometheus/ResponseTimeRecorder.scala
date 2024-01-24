package com.varwise.pekko.http.prometheus

import io.prometheus.client.{CollectorRegistry, Histogram}

import scala.concurrent.duration
import scala.concurrent.duration.{FiniteDuration, TimeUnit}

trait ResponseTimeRecorder {
  def recordResponseTime(endpoint: String, responseTime: FiniteDuration): Unit
}

/**
  * Records response times in Prometheus histogram.
  * Reported values will be automatically converted to the time unit set as constructor argument.
  *
  * @param metricName the metric name
  * @param metricHelp the metric help message
  * @param buckets the buckets that will be used in the histogram
  * @param endpointLabelName the endpoint label name that will be applied to the histogram when recording response times
  * @param registry a prometheus registry to which the histogram will be registered
  * @param timeUnit the time unit in which observed values will be recorded.
  */
class PrometheusResponseTimeRecorder(
    metricName: String,
    metricHelp: String,
    buckets: List[Double],
    endpointLabelName: String,
    registry: CollectorRegistry,
    timeUnit: TimeUnit)
    extends ResponseTimeRecorder {

  private val responseTimes = buildHistogram.register(registry)

  override def recordResponseTime(endpoint: String, responseTime: FiniteDuration): Unit =
    responseTimes.labels(endpoint).observe(responseTime.toUnit(timeUnit))

  private def buildHistogram: Histogram.Builder =
    Histogram
      .build()
      .name(metricName)
      .help(metricHelp)
      .labelNames(endpointLabelName)
      .buckets(buckets: _*)

}

object PrometheusResponseTimeRecorder {

  val DefaultBuckets = List(.01, .025, .05, .075, .10, .125, .15, .175, .20, .225, .25, .275, .30, .325, .35, .40, .45,
    .50, .60, .70, 1.0, 2.0, 3.0, 5.0, 10.0)
  val DefaultMetricName = "request_processing_seconds"
  val DefaultMetricHelp = "Time spent processing request"
  val DefaultEndpointLabel = "endpoint"
  val DefaultTimeUnit: TimeUnit = duration.SECONDS

  lazy val DefaultRegistry: CollectorRegistry = CollectorRegistry.defaultRegistry

  lazy val Default: PrometheusResponseTimeRecorder = {
    new PrometheusResponseTimeRecorder(
      DefaultMetricName,
      DefaultMetricHelp,
      DefaultBuckets,
      DefaultEndpointLabel,
      DefaultRegistry,
      DefaultTimeUnit
    )
  }
}

class NoOpResponseTimeRecorder extends ResponseTimeRecorder {
  def recordResponseTime(endpoint: String, responseTime: FiniteDuration): Unit = ()
}
