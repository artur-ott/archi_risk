name          := "htwg-scala-seed"
organization  := "de.htwg.se"
version       := "0.0.1"
scalaVersion  := "2.12.2"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")
mainClass := Some("ScalaRisk")

// resolvers += Resolver.jcenterRepo

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.4"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

libraryDependencies += "org.scala-lang" % "scala-swing" % "2.11+"
libraryDependencies += "junit" % "junit" % "4.8" % "test"
libraryDependencies += "net.codingwell" %% "scala-guice" % "4.1.0"
libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
libraryDependencies += "log4j" % "log4j" % "1.2.17"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test,
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.11",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test
)

libraryDependencies += "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.3.1"

// import com.github.retronym.SbtOneJar._

// oneJarSettings

// or if using sbt version < 0.13
// seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

// libraryDependencies += "commons-lang" % "commons-lang" % "2.6"