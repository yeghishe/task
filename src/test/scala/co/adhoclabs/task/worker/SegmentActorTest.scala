package co.adhoclabs.task.worker

import akka.actor.{ ActorRef, ActorSystem }
import akka.testkit.{ ImplicitSender, TestKit }
import co.adhoclabs.task.Config
import co.adhoclabs.task.client.SegmentClient
import co.adhoclabs.task.message._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import scala.concurrent.{ ExecutionContext, Future }
import scalaz.{ ∨, EitherT }
import scalaz.syntax.either._

/**
 * Created by yeghishe on 7/13/15.
 */
class SegmentActorTest extends TestKit(ActorSystem("test-system"))
    with Config
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with MockFactory
    with BeforeAndAfterAll {

  private implicit val segmentClient = mock[SegmentClient]
  import scala.concurrent.ExecutionContext.Implicits.global

  "SegmentActor" when {
    "receives SegmentRevenueEvent message" should {
      val e = SegmentOrderEvent("123test", "testOrderId", List(SegmentOrderEventItem("25-credits", "credits", 500)))

      "call client and reply with ok" in withActor { actor ⇒
        // Arrange
        (segmentClient.orderEvent(_: SegmentOrderEvent)(_: ExecutionContext))
          .expects(e, *)
          .returning(EitherT(Future(true.right)))

        // Act
        actor ! e

        // Assert
        expectMsg(OK)
      }

      "call client and reply with Error if api returns error" in withActor { actor ⇒
        // Arrange
        val msg = "foo"
        (segmentClient.orderEvent(_: SegmentOrderEvent)(_: ExecutionContext))
          .expects(e, *)
          .returning(EitherT(Future((400, msg).left)))

        // Act
        actor ! e

        // Assert
        expectMsg(Error(msg))
      }

      "call client and reply with Error future is error" in withActor { actor ⇒
        // Arrange
        val msg = "foo"
        (segmentClient.orderEvent(_: SegmentOrderEvent)(_: ExecutionContext))
          .expects(e, *)
          .returning(EitherT(Future.failed[(Int, String) ∨ Boolean](new RuntimeException(msg))))

        // Act
        actor ! e

        // Assert
        expectMsg(Error(msg))
      }
    }
  }

  private def withActor(testCode: ActorRef => Any): Unit = {
    val actor = system.actorOf(SegmentActor.props(segmentClient))

    try {
      testCode(actor)
    } finally {
      system.stop(actor)
    }
  }
}
