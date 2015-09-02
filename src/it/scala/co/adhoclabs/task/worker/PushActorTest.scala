package co.adhoclabs.task.worker

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import co.adhoclabs.task.Config
import co.adhoclabs.task.client.{GcmClient, UAClient}
import co.adhoclabs.task.message._
import com.typesafe.config.ConfigFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class PushActorTest extends TestKit(ActorSystem("test-system"))
with Config
with ImplicitSender
with WordSpecLike
with Matchers
with MockFactory
with BeforeAndAfterAll {
  private lazy val taskConfig = ConfigFactory.load().getConfig("task")

  "PushActor" when {
    "receives PushMessage message" should {
      "send message to chris iphone" in withActor { actor =>
        val msg = PushMessage(
          "5abfe50e0cb7849f721c012ac7764b3cdea3125068a2ae95c0f09d8f1c10110b",
          PushMessageTypes.apns,
          "msg with badge 4",
          Option(4),
          Map.empty,
          None
        )

        actor ! msg

        expectMsg(OK)
          }
    }
  }

  private def withActor(testCode: ActorRef => Any): Unit = {
    val pushConfig = taskConfig.getConfig("push")
    val apnsCertificatePath = Option(pushConfig.getString("certificate-path"))
    val apnsCertificatePwd = Option(pushConfig.getString("certificate-password"))
    val actor = system.actorOf(PushActor.props(UAClient, GcmClient, apnsCertificatePath, apnsCertificatePwd))

    try {
      testCode(actor)
    } finally {
      system.stop(actor)
    }
  }
}
