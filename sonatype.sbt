ThisBuild / sonatypeProfileName := "com.varwise"

ThisBuild / publishMavenStyle := true

ThisBuild / licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

ThisBuild / homepage := Some(url("https://github.com/varwise/prometheus-pekko-http/"))

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/varwise/prometheus-pekko-http/"),
    "scm:git@github.com:varwise/prometheus-pekko-http.git"
  )
)

ThisBuild / developers := List(
  Developer(id = "wlk", name = "Wojciech Langiewicz", email = "", url = url("https://www.wlangiewicz.com"))
)
