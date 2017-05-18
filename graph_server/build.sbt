name := "graph_server"

version := "1.0"

lazy val `graph_server` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc ,
  anorm ,
  cache ,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.0.akka23",
  "net.codingwell" %% "scala-guice" % "4.0.0",
  ws )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  