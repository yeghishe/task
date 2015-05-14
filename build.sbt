organization := "co.adhoclabs"
name := "task"
version := "0.0.2"
scalaVersion := "2.11.5"
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  val scalazVersion = "7.1.1"
  Seq(
    "org.scalaz"                %% "scalaz-core"                 % scalazVersion,
    "org.scalaz"                %% "scalaz-effect"               % scalazVersion,
    "org.scalaz"                %% "scalaz-typelevel"            % scalazVersion,
    "com.typesafe.akka"         %% "akka-actor"                  % akkaVersion,
    "com.typesafe.akka"         %% "akka-camel"                  % akkaVersion,
    "org.apache.camel"           % "camel-rabbitmq"              % "2.15.2",
    "org.scalaz"                %% "scalaz-scalacheck-binding"   % scalazVersion % "it,test",
    "com.typesafe.akka"         %% "akka-testkit"                % akkaVersion   % "it,test",
    "org.scalatest"             %% "scalatest"                   % "2.2.4"       % "it,test",
    "org.scalamock"             %% "scalamock-scalatest-support" % "3.2"         % "it,test"
  )
}

lazy val root = project.in(file(".")).configs(IntegrationTest)
Defaults.itSettings
scalariformSettings
Revolver.settings
enablePlugins(JavaAppPackaging)

initialCommands := """|import co.adhoclabs.task._
                      |import akka.actor._
                      |import akka.pattern._
                      |import akka.util._
                      |import scala.concurrent._
                      |import scala.concurrent.duration._""".stripMargin
