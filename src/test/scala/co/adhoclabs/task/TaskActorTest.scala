package co.adhoclabs.task

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.testkit.{ EventFilter, ImplicitSender, TestKit, TestProbe }
import co.adhoclabs.task.TaskActor.Task
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

/**
 * Created by yeghishe on 5/11/15.
 */

class TaskActorTest extends TestKit(ActorSystem("test-system"))
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with MockFactory
    with BeforeAndAfterAll {

  "TaskActor" should {
    "send serialized payload to right producer" in withTaskActor { (taskActor, producerProbe, _) ⇒
      taskActor ! Task(TaskType.demo, TestPayload)

      Serializer.deserialize(producerProbe.expectMsgType[Array[Byte]]) should be(TestPayload)
    }

    "warn if other message is received" in withTaskActor { (taskActor, _, _) ⇒
      val msg = "foo"
      EventFilter.warning(message = s"Got unexpected message: $msg", occurrences = 1) intercept {
        taskActor ! msg
      }
    }
  }

  private def withTaskActor(testCode: (ActorRef, TestProbe, TestProbe) => Any): Unit = {
    val producerProbe = TestProbe()
    val consumerProbe = TestProbe()

    class TaskActorForTest extends TaskActor {
      override protected def createCommonProducerActor(taskType: TaskType): ActorRef = producerProbe.ref
      override protected def createCommonConsumerActor(taskType: TaskType,
        targetActorPath: String): ActorRef = consumerProbe.ref
    }
    val taskActor = system.actorOf(Props(new TaskActorForTest))

    try {
      testCode(taskActor, producerProbe, consumerProbe)
    } finally {
      system.stop(producerProbe.ref)
      system.stop(consumerProbe.ref)
      system.stop(taskActor)
    }
  }

  override protected def afterAll(): Unit = {
    system.shutdown()
  }
}
