package com.varwise.pekko.http.prometheus.api

import org.apache.pekko.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import org.apache.pekko.http.scaladsl.model._
import io.prometheus.client.Collector.MetricFamilySamples
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat

import java.io.{StringWriter, Writer}
import java.util

case class MetricFamilySamplesEntity(samples: util.Enumeration[MetricFamilySamples])

object MetricFamilySamplesEntity {
  private val mediaTypeParams = Map("version" -> "0.0.4")

  private val mediaType =
    MediaType.customWithFixedCharset("text", "plain", HttpCharsets.`UTF-8`, params = mediaTypeParams)

  def fromRegistry(collectorRegistry: CollectorRegistry): MetricFamilySamplesEntity =
    MetricFamilySamplesEntity(collectorRegistry.metricFamilySamples())

  def toPrometheusTextFormat(e: MetricFamilySamplesEntity): String = {
    val writer: Writer = new StringWriter()
    TextFormat.write004(writer, e.samples)

    writer.toString
  }

  implicit val metricsFamilySamplesMarshaller: ToEntityMarshaller[MetricFamilySamplesEntity] =
    Marshaller.withFixedContentType(mediaType)(s => HttpEntity(mediaType, toPrometheusTextFormat(s)))

}
