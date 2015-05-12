package co.adhoclabs.task

import akka.actor.{ Actor, ActorLogging, ActorRef, Props }
import co.adhoclabs.task.TaskActor.Task

/**
 * Created by yeghishe on 5/4/15.
 */
object TaskActor {
  def props: Props = Props(new TaskActor)
  case class Task(taskType: TaskType, payload: Any)
}

class TaskActor extends Actor with ActorLogging with Config {
  var producersRefs = Map.empty[TaskType, ActorRef]

  protected def createCommonProducerActor(taskType: TaskType): ActorRef =
    context.actorOf(CommonProducerActor.props(taskType), s"producer-${taskType.name}")
  protected def createCommonConsumerActor(taskType: TaskType, targetActorPath: String): ActorRef =
    context.actorOf(CommonConsumerActor.props(taskType, targetActorPath), s"consumer-${taskType.name}")

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.debug(s"Starting producers: $producers")
    producersRefs = producers.map(t ⇒ t -> createCommonProducerActor(t)).toMap

    log.debug(s"Starting consumers: $consumerActorPaths")
    consumerActorPaths.foreach { case (t, p) ⇒ createCommonConsumerActor(t, p) }
  }

  override def receive: Receive = {
    case task @ Task(taskType, payload) ⇒
      log.debug(s"Publishing task: $task")
      producersRefs.get(taskType).foreach(_ ! Serializer.serialize(payload))
    case msg ⇒ log.warning(s"Got unexpected message: $msg")
  }
}
