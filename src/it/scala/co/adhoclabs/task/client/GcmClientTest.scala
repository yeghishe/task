package co.adhoclabs.task.client

import co.adhoclabs.task.message.{PushMessageTypes, PushMessage}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{Matchers, WordSpec}
import org.typelevel.scalatest.DisjunctionMatchers

class GcmClientTest extends WordSpec with ScalaFutures with Matchers with DisjunctionMatchers {
  import scala.concurrent.ExecutionContext.Implicits.global
  override implicit def patienceConfig: PatienceConfig =
    PatienceConfig(timeout = Span(20, Seconds), interval = Span(500, Millis))

  "GcmClient" when {
    "send" should {
      "return true right" in {
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

        GcmClient.send(msg).run.futureValue should beRight(true)
      }
    }
  }
}
