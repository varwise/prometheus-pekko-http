# prometheus-pekko-http

Collection of utilities to allow exposing prometheus metrics from pekko-http endpoint using the prometheus java client

    "com.varwise" %% "prometheus-pekko-http" % "1.0.0"

### Related projects

[prometheus-akka-http](https://github.com/varwise/prometheus-akka-http) - same thing but for akka-http

### Publishing

Artifacts are published to Maven central using the [sbt-sonatype](https://github.com/xerial/sbt-sonatype) plugin

Building howto:
```
sbt
; + publishSigned; sonatypeBundleRelease
```