name := "graph_client"

version := "1.0"

lazy val `graph_client` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"


libraryDependencies ++= Seq(
  "net.databinder.dispatch" % "dispatch-core_2.10" % "0.11.3",
  "com.google.inject" % "guice" % "3.0",
  "org.webjars" % "webjars-play_2.10" % "2.3.0-3",
  jdbc ,
  anorm ,
  cache ,
  ws
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

