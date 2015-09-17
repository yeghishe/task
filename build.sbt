organization := "io.github.yeghishe"
name := "task"
version := "1.0-SNAPSHOT"
scalaVersion := "2.11.7"
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

libraryDependencies ++= {
  val akkaVersion      = "2.3.9"
  val scalazVersion    = "7.1.1"
  val scalazScalaTestV = "0.2.3"
  val raptureV         = "1.0.0"

  Seq(
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

publishMavenStyle := true
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
pomExtra := (
  <url>http://yeghishe.github.io/</url>
  <licenses>
    <license>
      <name>Apache-2.0</name>
      <url>http://opensource.org/licenses/Apache-2.0</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>https://github.com/yeghishe/task</url>
    <connection>scm:git:git@github.com:yeghishe/task.git</connection>
  </scm>
  <developers>
    <developer>
      <id>ypiruzyan</id>
      <name>Yeghishe Piruzyan</name>
      <url>http://yeghishe.github.io/</url>
    </developer>
  </developers>)
