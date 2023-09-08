//import Dependencies._
import sbt.util
import java.io.File

//ThisBuild / scalaVersion     := "2.13.11"
//ThisBuild / version          := "0.1.0-SNAPSHOT"
//ThisBuild / organization     := "com.example"
//ThisBuild / organizationName := "example"
/*
lazy val root = (project in file("."))
  .settings(
    name := "secret-manager-poc",
    libraryDependencies += munit % Test
  )
*/
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.




// Keep these in-sync with the EMR release in use.
// Current EMR Release (EMR-5.36.0): https://docs.aws.amazon.com/emr/latest/ReleaseGuide/emr-5360-release.html
val awsSdkVersion = "1.12.438"
val sparkVersion = "2.4.8"
val jacksonVersion = "2.14.2"

val scalaVersionString = "2.11.12"
organization := "com.episource"

name := "secretsmanager-poc"

// the following line gets the env variable VERSION_TAG which is set in Travis before_script
// to whatever is set in the version_tag file
// for local development, it will use the tag "LOCAL_VERSION" or you can update getOrElse(x)
version := "0.1.0-SNAPSHOT"

scalaVersion := scalaVersionString

logLevel := util.Level.Info


coverageMinimumStmtTotal := 5
coverageFailOnMinimum := true

crossScalaVersions := Seq(scalaVersionString)

scalacOptions += "-target:jvm-1.8"

Test / logBuffered := false
Test / parallelExecution := false
assembly / test := {}

resolvers += "maven-central" at "https://repo1.maven.org/maven2/"

val baseDependencies = Seq(
  "com.typesafe" % "config" % "1.4.2",
  "joda-time" % "joda-time" % "2.12.4",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "6.4.0.jre8",
  "org.scalaj" %% "scalaj-http" % "2.4.2",
  "org.log4s" %% "log4s" % "1.10.0",
  "org.postgresql" % "postgresql" % "42.6.0",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test excludeAll ExclusionRule(organization = "com.fasterxml.jackson.core"),
  "com.github.tomakehurst" % "wiremock" % "2.27.2" % Test excludeAll ExclusionRule(organization = "com.fasterxml.jackson.core"),
  "net.liftweb" %% "lift-json" % "3.5.0",
  "com.lihaoyi" %% "os-lib" % "0.9.1"
)

/* To run the test locally this dependencies overwrite are needed. */
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion % Test
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion % Test
dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % jacksonVersion % Test


val dependencies = baseDependencies ++ {
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
    "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
    "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
    "org.apache.spark" %% "spark-hive" % sparkVersion % "provided",
    "com.amazonaws" % "aws-java-sdk" % awsSdkVersion % "provided"
  )
}

libraryDependencies ++= dependencies

// Uber Jar settings

assembly / assemblyMergeStrategy := {
  case m if m.startsWith("META-INF/services/") => MergeStrategy.concat
  case m if m.startsWith("META-INF/maven/") => MergeStrategy.discard
  case m if m.endsWith(".DSA") => MergeStrategy.discard
  case m if m.endsWith(".RSA") => MergeStrategy.discard
  case m if m.endsWith(".SF") => MergeStrategy.discard
  case m if Seq("MANIFEST.MF", "LICENSE.txt", "NOTICE.txt").exists("META-INF" + File.separator + _ == m) => MergeStrategy.discard

  case m if m == "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}

Compile / run / mainClass := Some("main.scala.example.Main")

// Publish artifacts to S3
publishMavenStyle := true
publishTo := Some("Nitrogen Snapshots" at "s3://epi-nitrogen/repo")

Compile / assembly / artifact := {
  val art = (Compile / assembly / artifact).value
  art.withClassifier(Some("assembly"))
}

addArtifact(Compile / assembly / artifact, assembly)
