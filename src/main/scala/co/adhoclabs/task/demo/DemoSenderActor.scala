package co.adhoclabs.task.demo

import akka.actor._
import co.adhoclabs.task.{ TaskActor, TaskType }

import scala.concurrent.duration._

/**
 * Created by yeghishe on 5/4/15.
 */
object DemoSenderActor {
  def props(taskActor: ActorRef): Props = Props(new DemoSenderActor(taskActor))

}
class DemoSenderActor(taskActor: ActorRef) extends Actor with ActorLogging {
  private case object Send
  private var i = 1

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    import context.dispatcher
    context.system.scheduler.schedule(1.second, 3.second, self, Send)
  }

  override def receive: Receive = {
    case Send ⇒
      taskActor ! TaskActor.Task(TaskType.demo, DemoPayload(i))
      i = i + 1
  }
}
