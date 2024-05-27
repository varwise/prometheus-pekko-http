name := "prometheus-pekko-http"

organization := "com.varwise"

publishTo := sonatypePublishToBundle.value

version := "2.0.0"

val scala2Version = "2.13.14"
val scala3Version = "3.3.3"

scalaVersion := scala3Version
crossScalaVersions := Seq(scala3Version, scala2Version)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

libraryDependencies ++= {
  val prometheusJavaClientVersion = "1.3.1"
  val pekkoVersion = "1.0.2"
  val pekkoHttpVersion = "1.0.1"
  val scalaTestVersion = "3.2.18"
  val slf4jVersion = "2.0.13"

  Seq(
    "org.apache.pekko" %% "pekko-actor"                           % pekkoVersion     % Provided,
    "org.apache.pekko" %% "pekko-stream"                          % pekkoVersion     % Provided,
    "org.apache.pekko" %% "pekko-http"                            % pekkoHttpVersion % Provided,
    "org.apache.pekko" %% "pekko-http-spray-json"                 % pekkoHttpVersion % Provided,
    "io.prometheus"     % "prometheus-metrics-core"               % prometheusJavaClientVersion,
    "io.prometheus"     % "prometheus-metrics-exposition-formats" % prometheusJavaClientVersion,
    "org.slf4j"         % "slf4j-api"                             % slf4jVersion,
    "org.apache.pekko" %% "pekko-testkit"                         % pekkoVersion     % Test,
    "org.apache.pekko" %% "pekko-http-testkit"                    % pekkoHttpVersion % Test,
    "org.scalatest"    %% "scalatest"                             % scalaTestVersion % Test
  )
}

lazy val `root` = (project in file("."))
  .settings(
    addCommandAlias("testAll", ";test"),
    addCommandAlias("formatAll", ";scalafmt;Test/scalafmt;scalafmtSbt"),
    addCommandAlias("compileAll", ";compile;Test/compile")
  )
