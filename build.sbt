
name := "akka-playground"
version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"
scalacOptions ++= Seq("-encoding", "UTF-8", "-deprecation", "-feature", "-unchecked", "-Xlint")

val akkaV = "2.4.8"
val akkaStreamV = "2.0-M1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-persistence" % akkaV,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaStreamV,
  "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamV,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV,
  "com.typesafe.akka" % "akka-slf4j_2.11" % akkaV,


  "com.typesafe.scala-logging" %% "scala-logging"  % "3.1.0",
  "ch.qos.logback"             % "logback-classic" % "1.1.3",

  "org.iq80.leveldb"            % "leveldb"          % "0.7",
  "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8"
)
