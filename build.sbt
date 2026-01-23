ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.7"

lazy val root = (project in file("."))
  .settings(
    name := "PROJET"
  )
libraryDependencies += "io.circe" %% "circe-core" % "0.14.10"
libraryDependencies += "io.circe" %% "circe-parser" % "0.14.10"
libraryDependencies += "io.circe" %% "circe-generic" % "0.14.10"
