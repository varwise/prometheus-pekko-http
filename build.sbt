name := "prometheus-pekko-http"

organization := "com.varwise"

publishTo := sonatypePublishToBundle.value

version := "1.0.0"

val scala2Version = "2.13.12"
val scala3Version = "3.3.1"

scalaVersion := scala3Version
crossScalaVersions := Seq(scala3Version, scala2Version)

credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

libraryDependencies ++= {
  val simpleclientVersion = "0.16.0"
  val pekkoVersion = "1.0.2"
  val pekkoHttpVersion = "1.0.0"
  val scalaTestVersion = "3.2.17"

  Seq(
    ("org.apache.pekko" %% "pekko-actor"           % pekkoVersion     % Provided).cross(CrossVersion.for3Use2_13),
    ("org.apache.pekko" %% "pekko-stream"          % pekkoVersion     % Provided).cross(CrossVersion.for3Use2_13),
    ("org.apache.pekko" %% "pekko-http"            % pekkoHttpVersion % Provided).cross(CrossVersion.for3Use2_13),
    ("org.apache.pekko" %% "pekko-http-spray-json" % pekkoHttpVersion % Provided).cross(CrossVersion.for3Use2_13),
    "io.prometheus"      % "simpleclient"          % simpleclientVersion,
    "io.prometheus"      % "simpleclient_common"   % simpleclientVersion,
    ("org.apache.pekko" %% "pekko-testkit"         % pekkoVersion     % Test).cross(CrossVersion.for3Use2_13),
    ("org.apache.pekko" %% "pekko-http-testkit"    % pekkoHttpVersion % Test).cross(CrossVersion.for3Use2_13),
    "org.scalatest"     %% "scalatest"             % scalaTestVersion % Test
  )
}

lazy val `root` = (project in file("."))
  .settings(
    addCommandAlias("testAll", ";test"),
    addCommandAlias("formatAll", ";scalafmt;Test/scalafmt;scalafmtSbt"),
    addCommandAlias("compileAll", ";compile;Test/compile")
  )
