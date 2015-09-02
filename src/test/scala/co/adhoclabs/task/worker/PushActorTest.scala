package co.adhoclabs.task.worker

import akka.actor.{ ActorRef, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit }
import co.adhoclabs.task.Config
import co.adhoclabs.task.client.{ GcmClient, UAClient }
import co.adhoclabs.task.message._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import scala.concurrent.{ ExecutionContext, Future }
import scalaz.EitherT
import scalaz.syntax.either._

class PushActorTest extends TestKit(ActorSystem("test-system"))
    with Config
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with MockFactory
    with BeforeAndAfterAll {

  private implicit val uaClient: UAClient = mock[UAClient]
  private implicit val gcmClient: GcmClient = mock[GcmClient]

  import scala.concurrent.ExecutionContext.Implicits.global

  "PushActor" when {
    "receives PushMessage message" should {
      "send message to ua and return with ok" in withActor { actor =>
        val msg = PushMessage(
          "6ac334cd-1fad-4c15-98c5-a62eb0fba4a6",
          PushMessageTypes.ua,
          "testing msg from thu sep 10th - ua",
          None,
          Map(
            "type" -> "sms",
            "number" -> "+16669998888",
            "bid" -> "2"
          ),
          None
        )

        (uaClient.send(_: PushMessage)(_: ExecutionContext))
          .expects(msg, *)
          .returning(EitherT(Future(true.right)))

        actor ! msg

        expectMsg(OK)
      }

      "send message to gcm and return with ok" in withActor { actor =>
        val msg = PushMessage(
          "eV7utTJfnVI:APA91bE5KZftsmawPZ4386t62wD6rZ3QCEiWa86wIJxPrbqp3JlvidjYAI8UeRUhLvjlXz1QK9ef5LiXbKJdcdm2138kTJ435_jeWGzzzY6MoDXzyuX_dYTHX8ayEViDCg9pCU3YTasH",
          PushMessageTypes.gcm,
          "snd testing msg from thu sep 10th - gcm",
          None,
          Map(
            "type" -> "sms",
            "bid" -> "burnerId"
          ),
          None
        )

        (gcmClient.send(_: PushMessage)(_: ExecutionContext))
          .expects(msg, *)
          .returning(EitherT(Future(true.right)))

        actor ! msg

        expectMsg(OK)
      }
    }
  }

  private def withActor(testCode: ActorRef => Any): Unit = {
    val actor = system.actorOf(PushActor.props(uaClient, gcmClient, None, None))

    try {
      testCode(actor)
    } finally {
      system.stop(actor)
    }
  }
}
