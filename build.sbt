enablePlugins(AutomateHeaderPlugin)

name := "exts"
organization := "nl.salp.exts"
headerLicense := Some(HeaderLicense.ALv2("2018", "Barre Dijkstra"))

scalaVersion := "2.13.1"
crossScalaVersions := Seq("2.13.1", "2.12.10")
resolvers += Resolver.mavenLocal

autoScalaLibrary := false
libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.4.+",
  "io.spray" %% "spray-json" % "1.3.+",
  "com.beachape" %% "enumeratum" % "1.5.+"
) map (_ % Provided)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.1.+"
) map (_ % Test)

scalacOptions in Compile ++= Seq(
  "-encoding", "UTF-8",
  "-deprecation",
  "-explaintypes",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:postfixOps",
  "-language:existentials",
  "-Xlint"
)

publishMavenStyle := true
publishArtifact in Test := false
organizationName := "Barre Dijkstra"
startYear := Some(2018)
licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0.txt"))
homepage := Some(url("https://github.com/barredijkstra/exts"))
scmInfo := Some(ScmInfo(
  browseUrl = url("https://github.com/barredijkstra/exts"),
  connection = "git@github.com:barredijkstra/exts.git"
))
pomExtra := {
  <url>https://github.com/barredijkstra/exts</url>
    <licenses>
      <license>
        <name>Apache2.0</name>
        <url>https://opensource.org/licenses/Apache-2.0</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:barredijkstra/exts.git</url>
      <connection>scm:git@github.com:barredijkstra/exts.git</connection>
    </scm>
    <developers>
      <developer>
        <id>barredijkstra</id>
        <name>barredijkstra</name>
        <url>http://github.com/barredijkstra</url>
      </developer>
    </developers>
}

releaseCrossBuild := true
