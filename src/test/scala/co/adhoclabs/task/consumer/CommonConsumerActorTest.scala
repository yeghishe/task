package co.adhoclabs.task.consumer

import akka.actor.{ ActorSystem, Status }
import akka.camel.{ Ack, CamelMessage }
import akka.testkit._
import co.adhoclabs.task._
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

/**
 * Created by yeghishe on 5/11/15.
 */
class CommonConsumerActorTest extends TestKit(ActorSystem("test-system"))
    with Config
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  private val taskType = TaskType.demo

  "CommonConsumerActor" should {
    "have endpointUri set correct" in withConsumerActor { (consumerActor, _) ⇒
      consumerActor.underlyingActor.endpointUri should be(getCommonEndpointUrlFromTaskType(taskType))
    }

    "send the message to target actor and pipe reply to self and send Ack to sender" in withConsumerActor { (consumerActor, targetProbe) ⇒
      val reply = "some reply"
      consumerActor ! CamelMessage(Serializer.serialize(TestPayload), Map.empty)

      targetProbe.expectMsg(TestPayload)
      targetProbe.reply(reply)
      expectMsg(Ack)
    }

    "log with info when getting Status.Failure" in withConsumerActor { (consumerActor, targetProbe) ⇒
      val error = new RuntimeException("err")
      val logMessage = s"${targetProbe.ref.path} failed to process message: $error"
      EventFilter.info(message = logMessage, occurrences = 1) intercept {
        consumerActor ! Status.Failure(error)
      }
    }

    "warn if other message is received" in withConsumerActor { (consumerActor, _) ⇒
      val msg = "foo"
      EventFilter.warning(message = s"Got unexpected message: $msg", occurrences = 1) intercept {
        consumerActor ! msg
      }
    }
  }

  private def withConsumerActor(testCode: (TestActorRef[CommonConsumerActor], TestProbe) => Any): Unit = {
    val targetProbe = TestProbe()
    val consumerActor = TestActorRef.create[CommonConsumerActor](
      system, CommonConsumerActor.props(taskType, targetProbe.ref.path)
    )

    try {
      testCode(consumerActor, targetProbe)
    } finally {
      system.stop(targetProbe.ref)
      system.stop(consumerActor)
    }
  }
}
