name := "prometheus-pekko-http"

organization := "com.varwise"

publishTo := sonatypePublishToBundle.value

version := "1.0.1"

val scala2Version = "2.13.13"
val scala3Version = "3.3.3"

scalaVersion := scala3Version
crossScalaVersions := Seq(scala3Version, scala2Version)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

libraryDependencies ++= {
  val simpleclientVersion = "0.16.0"
  val pekkoVersion = "1.0.2"
  val pekkoHttpVersion = "1.0.1"
  val scalaTestVersion = "3.2.18"

  Seq(
    "org.apache.pekko" %% "pekko-actor"           % pekkoVersion     % Provided,
    "org.apache.pekko" %% "pekko-stream"          % pekkoVersion     % Provided,
    "org.apache.pekko" %% "pekko-http"            % pekkoHttpVersion % Provided,
    "org.apache.pekko" %% "pekko-http-spray-json" % pekkoHttpVersion % Provided,
    "io.prometheus"      % "simpleclient"         % simpleclientVersion,
    "io.prometheus"      % "simpleclient_common"  % simpleclientVersion,
    "org.apache.pekko" %% "pekko-testkit"         % pekkoVersion     % Test,
    "org.apache.pekko" %% "pekko-http-testkit"    % pekkoHttpVersion % Test,
    "org.scalatest"     %% "scalatest"            % scalaTestVersion % Test
  )
}

lazy val `root` = (project in file("."))
  .settings(
    addCommandAlias("testAll", ";test"),
    addCommandAlias("formatAll", ";scalafmt;Test/scalafmt;scalafmtSbt"),
    addCommandAlias("compileAll", ";compile;Test/compile")
  )
