package co.adhoclabs.task.worker

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import co.adhoclabs.model.notifications.{PushMessageTypes, PushMessage}
import co.adhoclabs.task.Config
import co.adhoclabs.task.client.{GcmClient, UAClient}
import co.adhoclabs.task.message._
import com.typesafe.config.ConfigFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class PushActorIntegrationTest extends TestKit(ActorSystem("test-system"))
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
          "8855d9db9798debe3b94df220aff0cad1194a24fdbf13e808dbabe636a906411",
          PushMessageTypes.apns,
          "msg with badge 4",
          Option(4),
          Map("bid" → "1",
          "number" → "+13109998888",
          "type" → "sms"),
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
