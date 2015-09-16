package co.adhoclabs.task.client

import co.adhoclabs.model.notifications.{PushMessageTypes, PushMessage}
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
          "eORjNkcs3dU:APA91bEuOePQICwGI1nBgh8UJ03t6C2HnnOGcxUpuq6n7kInkljzV8mCDqJ5n07afqd9QqWyiv5bbz7me0sCJG4fDqX9plnLecol5ZjVS_-uqYo8MGITQfTkW48tt1B9acSEMaLGTFjw",
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
