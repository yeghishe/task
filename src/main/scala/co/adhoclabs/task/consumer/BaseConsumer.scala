package co.adhoclabs.task.consumer

import akka.actor.ActorLogging
import akka.camel.Consumer
import co.adhoclabs.task._

/**
 * Created by yeghishe on 7/13/15.
 */
trait BaseConsumer extends Consumer with ActorLogging with Config {
  protected def taskType: TaskType
  override def endpointUri: String = getCommonEndpointUrlFromTaskType(taskType)
}
