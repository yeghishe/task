package co.adhoclabs.task.producer

import akka.actor.ActorLogging
import akka.camel.Producer
import co.adhoclabs.task._

/**
 * Created by yeghishe on 7/13/15.
 */
trait BaseProducer extends Producer with ActorLogging with Config {
  protected def taskType: TaskType
  override def endpointUri: String = getCommonEndpointUrlFromTaskType(taskType)

  override def preStart(): Unit = {
    log.debug(s"Starting producer for $taskType")
    super.preStart()
  }
}
