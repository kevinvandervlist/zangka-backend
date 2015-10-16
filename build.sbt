name := "zangka-backend"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions := Seq("-feature", "-deprecation")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0",
  "org.scalatest" % "scalatest_2.11" % "3.0.0-M9" % "test"
)
