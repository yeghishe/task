package co.adhoclabs.task.consumer

import akka.actor.{ ActorPath, ActorLogging, Props, Status }
import akka.camel.{ Ack, CamelMessage, Consumer }
import akka.util.Timeout
import co.adhoclabs.task._

/**
 * Created by yeghishe on 5/4/15.
 */
object CommonConsumerActor {
  def props(taskType: TaskType, targetActorPath: ActorPath): Props =
    Props(new CommonConsumerActor(taskType, targetActorPath))
}

class CommonConsumerActor(override val taskType: TaskType, targetActorPath: ActorPath) extends BaseConsumer {
  private case class Reply(msg: Any)

  private val targetActor = context.actorSelection(targetActorPath)
  implicit private val askTimeout: Timeout = targetActorTimeout

  override def preStart(): Unit = {
    log.debug(s"Starting consumer for $taskType")
    super.preStart()
  }

  override def receive: Receive = {
    case CamelMessage(body: Array[Byte], _) ⇒
      import akka.pattern.{ ask, pipe }
      import context.dispatcher

      val msg = Serializer.deserialize(body)
      log.debug(s"Sending message $msg to $targetActorPath")
      targetActor.ask(msg).map(Reply).pipeTo(self)(sender())

    case Reply(_) ⇒
      log.debug("Sending ack.")
      sender ! Ack
    case Status.Failure(e) ⇒ log.info(s"$targetActorPath failed to process message: $e")
    case msg ⇒ log.warning(s"Got unexpected message: $msg")
  }
}
