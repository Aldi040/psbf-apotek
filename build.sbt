name := """apoteksehat"""
organization := "apoteksehat.com"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

// Dependensi untuk Play Framework dan Guice
libraryDependencies += guice

// Dependensi untuk testing dengan Play
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test

// Dependensi JDBC dan MySQL
libraryDependencies += jdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.28"


