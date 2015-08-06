package co.adhoclabs.task

import akka.actor._
import akka.routing.FromConfig
import co.adhoclabs.task.TaskActor.Task
import co.adhoclabs.task.consumer.{ CommonConsumerActor }
import co.adhoclabs.task.producer.CommonProducerActor

/**
 * Created by yeghishe on 5/4/15.
 */
object TaskActor {
  def props: Props = Props(new TaskActor)
  case class Task(taskType: TaskType, payload: Any)
}

class TaskActor extends Actor with ActorLogging with Config {
  var producerRefs = Map.empty[TaskType, ActorRef]

  protected def createProducerActor(taskType: TaskType): ActorRef =
    context.actorOf(CommonProducerActor.props(taskType), s"producer-${taskType.name}")

  protected def createProxyConsumerActor(taskType: TaskType, targetActorPath: String): ActorRef =
    context.actorOf(
      CommonConsumerActor.props(taskType, ActorPath.fromString(targetActorPath)),
      s"consumer-${taskType.name}"
    )

  protected def createConsumerActor(taskType: TaskType): ActorRef = {
    val w = context.actorOf(workerActorPropMap(taskType), s"worker-${taskType.name}")
    context.actorOf(
      FromConfig.props(CommonConsumerActor.props(taskType, w.path)),
      s"consumer-${taskType.name}"
    )
  }

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.debug(s"Starting producers: $producers")
    producerRefs = producers.map(t ⇒ t -> createProducerActor(t)).toMap

    log.debug(s"Starting consumers: $consumers, $consumerProxyActors")

    consumers.map { t ⇒
      consumerProxyActors.get(t).fold(createConsumerActor(t))(createProxyConsumerActor(t, _))
    }
  }

  override def receive: Receive = {
    case task @ Task(taskType, payload) ⇒
      log.debug(s"Publishing task: $task")
      producerRefs.get(taskType).foreach(_ forward Serializer.serialize(payload))
    case msg ⇒ log.warning(s"Got unexpected message: $msg")
  }
}
