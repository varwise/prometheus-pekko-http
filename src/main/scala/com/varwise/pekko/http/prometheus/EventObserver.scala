package com.varwise.pekko.http.prometheus

import io.prometheus.metrics.core.metrics.Counter
import io.prometheus.metrics.model.registry.PrometheusRegistry

trait EventObserver {
  def observe(eventName: String, eventDetails: String): Unit
}

/** Records observed events in a prometheus counter
  *
  * @param metricName
  *   the metric name
  * @param metricHelp
  *   the metric help message
  * @param eventLabelName
  *   the event label name that will be applied to the counter when recording events
  * @param eventDetailsLabelName
  *   the event details label name that will be applied to the counter when recording events
  * @param registry
  *   a prometheus registry to which the counter will be registered
  */
class PrometheusEventObserver(
    metricName: String,
    metricHelp: String,
    eventLabelName: String,
    eventDetailsLabelName: String,
    registry: PrometheusRegistry)
    extends EventObserver {

  private val counter: Counter =
    Counter
      .builder()
      .name(metricName)
      .help(metricHelp)
      .labelNames(eventLabelName, eventDetailsLabelName)
      .register(registry)

  override def observe(eventName: String, eventDetails: String): Unit =
    counter.labelValues(eventName, eventDetails).inc()
}

object PrometheusEventObserver {
  private val SuccessfulOperationMetricName = "operation_success"
  private val SuccessfulOperationMetricHelp = "The number of observed successful operations"
  private val FailedOperationMetricName = "operation_failure"
  private val FailedOperationMetricHelp = "The number of observed failed operations"
  private val DefaultEventLabelName = "event"
  private val DefaultEventDetailsLabelName = "details"
  private val DefaultRegistry = PrometheusRegistry.defaultRegistry

  // Common event observers used in scala projects in Open Planet micro-services
  lazy val SuccessfulOperations: PrometheusEventObserver =
    withDefaultsFromMetricNameAndHelp(SuccessfulOperationMetricName, SuccessfulOperationMetricHelp)

  lazy val FailedOperations: PrometheusEventObserver =
    withDefaultsFromMetricNameAndHelp(FailedOperationMetricName, FailedOperationMetricHelp)

  private def withDefaultsFromMetricNameAndHelp(metricName: String, metricHelp: String) =
    new PrometheusEventObserver(
      metricName,
      metricHelp,
      DefaultEventLabelName,
      DefaultEventDetailsLabelName,
      DefaultRegistry
    )
}

class NoOpEventObserver extends EventObserver {
  def observe(eventName: String, eventDetails: String): Unit = ()
}
