import sbt._

object Dependencies {

  object V {
    val slf4j = "1.7.16"
    val logBack = "1.1.5"
    val scalaLogging = "3.1.0"
    val akka = "2.4.2"
    val akkaTwirl = "1.0"
    val scopt = "3.4.0"
    val configs = "0.3.0"
    val macwire = "2.2.2"
    val scalaTest = "2.2.5"

    val jquery = "2.2.1"
  }

  // config

  val scopt = "com.github.scopt" %% "scopt" % V.scopt
  val macwire = "com.softwaremill.macwire" %% "macros" % V.macwire
  val configs = "com.github.kxbmap" %% "configs" % V.configs

  val configStack = Seq(scopt, macwire, configs)

  // common

  val java8Compat = "org.scala-lang.modules" %% "scala-java8-compat" % "0.7.0"

  val commonStack = Seq(java8Compat)

  // akka

  val akkaHttp = "com.typesafe.akka" %% "akka-http-experimental" % V.akka
  val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % V.akka % "test"
  val akkaStack = Seq(akkaHttp, akkaHttpTestKit)

  // web

  val akkaTwirl = "io.buddho.akka"  %% "akka-http-twirl" % V.akkaTwirl
  val jquery = "org.webjars.bower" % "jquery" % V.jquery

  val webStack = Seq(akkaTwirl, jquery)

  // logging

  val slf4jApi = "org.slf4j" % "slf4j-api" % V.slf4j
  val logBackClassic = "ch.qos.logback" % "logback-classic" % V.logBack
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % V.scalaLogging

  val loggingStack = Seq(slf4jApi, logBackClassic, scalaLogging)

  // testing

  val scalatest = "org.scalatest" %% "scalatest" % V.scalaTest % "test"

  val testStack = Seq(scalatest)

}