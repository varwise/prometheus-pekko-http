package com.varwise.pekko.http.prometheus

import io.prometheus.metrics.core.metrics.Histogram
import io.prometheus.metrics.model.registry.PrometheusRegistry

import scala.concurrent.duration
import scala.concurrent.duration.{FiniteDuration, TimeUnit}

trait ResponseTimeRecorder {
  def recordResponseTime(endpoint: String, responseTime: FiniteDuration): Unit
}

/** Records response times in Prometheus histogram. Reported values will be automatically converted to the time unit set
  * as constructor argument.
  *
  * @param metricName
  *   the metric name
  * @param metricHelp
  *   the metric help message
  * @param buckets
  *   the buckets that will be used in the histogram
  * @param endpointLabelName
  *   the endpoint label name that will be applied to the histogram when recording response times
  * @param registry
  *   a prometheus registry to which the histogram will be registered
  * @param timeUnit
  *   the time unit in which observed values will be recorded.
  */
class PrometheusResponseTimeRecorder(
    metricName: String,
    metricHelp: String,
    buckets: List[Double],
    endpointLabelName: String,
    registry: PrometheusRegistry,
    timeUnit: TimeUnit)
    extends ResponseTimeRecorder {

  private val responseTimes: Histogram =
    Histogram
      .builder()
      .name(metricName)
      .help(metricHelp)
      .labelNames(endpointLabelName)
      .classicUpperBounds(buckets: _*)
      .register(registry)

  override def recordResponseTime(endpoint: String, responseTime: FiniteDuration): Unit =
    responseTimes.labelValues(endpoint).observe(responseTime.toUnit(timeUnit))
}

object PrometheusResponseTimeRecorder {

  val DefaultBuckets = List(.01, .025, .05, .075, .10, .125, .15, .175, .20, .225, .25, .275, .30, .325, .35, .40, .45,
    .50, .60, .70, 1.0, 2.0, 3.0, 5.0, 10.0)
  val DefaultMetricName = "request_processing_seconds"
  val DefaultMetricHelp = "Time spent processing request"
  val DefaultEndpointLabel = "endpoint"
  val DefaultTimeUnit: TimeUnit = duration.SECONDS

  lazy val DefaultRegistry: PrometheusRegistry = PrometheusRegistry.defaultRegistry

  lazy val Default: PrometheusResponseTimeRecorder =
    new PrometheusResponseTimeRecorder(
      DefaultMetricName,
      DefaultMetricHelp,
      DefaultBuckets,
      DefaultEndpointLabel,
      DefaultRegistry,
      DefaultTimeUnit
    )
}

class NoOpResponseTimeRecorder extends ResponseTimeRecorder {
  def recordResponseTime(endpoint: String, responseTime: FiniteDuration): Unit = ()
}
