
name := "akka-playground"
version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"
scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-unchecked", "-Xlint")

val akkaV = "2.4.1"
val akkaStreamV = "2.0-M1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaStreamV,
  "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamV,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV,


  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback"             % "logback-classic" % "1.1.3"

)
