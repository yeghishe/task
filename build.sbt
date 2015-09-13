organization := "co.adhoclabs"
name := "task"
version := "0.1.2"
scalaVersion := "2.11.5"
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= {
  val akkaVersion      = "2.3.9"
  val scalazVersion    = "7.1.1"
  val scalazScalaTestV = "0.2.3"
  val raptureV         = "1.0.0"

  Seq(
    "co.adhoclabs"              %% "model"                       % "1.3.8",
    "org.scalaz"                %% "scalaz-core"                 % scalazVersion,
    "org.scalaz"                %% "scalaz-effect"               % scalazVersion,
    "org.scalaz"                %% "scalaz-typelevel"            % scalazVersion,
    "com.typesafe.akka"         %% "akka-actor"                  % akkaVersion,
    "com.typesafe.akka"         %% "akka-camel"                  % akkaVersion,
    "com.propensive"            %% "rapture-core"                % raptureV,
    "com.propensive"            %% "rapture-json"                % raptureV,
    "com.propensive"            %% "rapture-json-jackson"        % raptureV,
    "com.propensive"            %% "rapture-uri"                 % raptureV,
    "com.propensive"            %% "rapture-codec"               % raptureV,
    "com.propensive"            %% "rapture-io"                  % "0.10.0",
    "com.propensive"            %% "rapture-net"                 % "0.10.0",
    "org.apache.camel"           % "camel-rabbitmq"              % "2.15.2",
    "org.apache.camel"           % "camel-apns"                  % "2.15.2",
    "org.scalaz"                %% "scalaz-scalacheck-binding"   % scalazVersion    % "it,test",
    "org.typelevel"             %% "scalaz-scalatest"            % scalazScalaTestV % "it,test",
    "com.typesafe.akka"         %% "akka-testkit"                % akkaVersion      % "it,test",
    "org.scalatest"             %% "scalatest"                   % "2.2.4"          % "it,test",
    "org.scalamock"             %% "scalamock-scalatest-support" % "3.2"            % "it,test",
    "com.urbanairship"           % "java-client"                 % "0.3.3"
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
