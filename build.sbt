name := """pet.management.system"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(filters, cache, evolutions)
libraryDependencies ++= Seq(filters, cache, evolutions)
