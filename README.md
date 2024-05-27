# prometheus-pekko-http

Collection of utilities to allow exposing prometheus metrics from pekko-http endpoint using the prometheus java client

    "com.varwise" %% "prometheus-pekko-http" % "1.0.1"

### Related projects

[prometheus-akka-http](https://github.com/varwise/prometheus-akka-http) - same thing but for akka-http

### Migration to 2.0.0

Version 2.0.0 uses Prometheus Java client library 1.0.0 which introduced some breaking changes.

Here's the quick migration guide:
1. Replace occurrences of `io.prometheus.client.CollectorRegistry` with `io.prometheus.metrics.model.registry.PrometheusRegistry`. 
2. Supply `ExecutionContext` instance to `MetricsEndpoint`.

For more information about Prometheus Java client library please refer to https://prometheus.github.io/client_java/.
### Publishing

Artifacts are published to Maven central using the [sbt-sonatype](https://github.com/xerial/sbt-sonatype) plugin

Building howto:
```
sbt
; + publishSigned; sonatypeBundleRelease
```