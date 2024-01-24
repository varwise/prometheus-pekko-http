# prometheus-pekko-http

Collection of utilities to allow exposing prometheus metrics from pekko-http endpoint using the prometheus java client

    "com.varwise" %% "prometheus-pekko-http" % "0.6.0"

### Publishing

Artifacts are published to Maven central using the [sbt-sonatype](https://github.com/xerial/sbt-sonatype) plugin

Building howto:
```
sbt
; + publishSigned; sonatypeBundleRelease
```