name := """apoteksehat"""
organization := "apoteksehat.com"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "apoteksehat.com.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "apoteksehat.com.binders._"
libraryDependencies += jdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.28"
