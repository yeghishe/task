package co.adhoclabs.task.client

import co.adhoclabs.task.message.{SegmentOrderEventItem, SegmentOrderEvent}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{Matchers, WordSpec}
import org.typelevel.scalatest.DisjunctionMatchers

/**
 * Created by yeghishe on 7/13/15.
 */
class SegmentClientTest extends WordSpec with ScalaFutures with Matchers with DisjunctionMatchers {
  import scala.concurrent.ExecutionContext.Implicits.global
  override implicit def patienceConfig: PatienceConfig =
    PatienceConfig(timeout = Span(20, Seconds), interval = Span(500, Millis))

  "SegmentClient" when {
    "trackRevenue" should {
      "return true right" in {
        val e = SegmentOrderEvent("123test", "testOrderId", List(SegmentOrderEventItem("25-credits", "credits", 500)))
        SegmentClient.orderEvent(e).run.futureValue should beRight(true)
      }
    }
  }
}
