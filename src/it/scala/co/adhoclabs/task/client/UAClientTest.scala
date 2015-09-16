package co.adhoclabs.task.client

import co.adhoclabs.model.notifications.{PushMessageTypes, PushMessage}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{Matchers, WordSpec}
import org.typelevel.scalatest.DisjunctionMatchers

class UAClientTest extends WordSpec with ScalaFutures with Matchers with DisjunctionMatchers {
  import scala.concurrent.ExecutionContext.Implicits.global
  override implicit def patienceConfig: PatienceConfig =
    PatienceConfig(timeout = Span(20, Seconds), interval = Span(500, Millis))

  "UAClient" when {
    "send" should {
      "return true right" in {
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

        UAClient.send(msg).run.futureValue should beRight(true)
      }
    }
  }
}
