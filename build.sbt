enablePlugins(AutomateHeaderPlugin)

name := "exts"
organization := "nl.salp.exts"
headerLicense := Some(HeaderLicense.ALv2("2018", "Barre Dijkstra"))

scalaVersion := "2.12.6"
crossScalaVersions := Seq("2.12.6", "2.11.12")
resolvers += Resolver.mavenLocal

autoScalaLibrary := false
libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.+",
  "io.spray" %%  "spray-json" % "1.3.+",
  "com.beachape" %% "enumeratum" % "1.5.+"
) map (_ % Provided)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.+"
) map (_ % Test)

scalacOptions in Compile ++= Seq(
  "-encoding", "UTF-8",
  "-deprecation",
  "-explaintypes",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-Xlint",
  "-Ywarn-dead-code",
  "-Ywarn-infer-any",
  "-Ywarn-inaccessible"
) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, majorVersion)) if majorVersion >= 12 =>
    Seq("-target:jvm-1.8")
  case _ =>
    Seq("-target:jvm-1.7")
})


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
import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  /*
    commitReleaseVersion,
    tagRelease,
    releaseStepCommandAndRemaining("+ publish"),
    releaseStepTask(bintrayRelease),
  */
  publishArtifacts,
  setNextVersion
  /*
    setNextVersion,
    commitNextVersion,
    pushChanges
  */
)
