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

libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.2"
libraryDependencies += "com.h2database" % "h2" % "1.4.185"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"


// import com.github.retronym.SbtOneJar._

// oneJarSettings

// or if using sbt version < 0.13
// seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

// libraryDependencies += "commons-lang" % "commons-lang" % "2.6"