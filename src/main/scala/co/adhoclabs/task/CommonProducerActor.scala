package co.adhoclabs.task

import akka.actor.Props
import akka.camel.{ Oneway, Producer }

/**
 * Created by yeghishe on 5/4/15.
 */
object CommonProducerActor {
  def props(taskType: TaskType): Props = Props(new CommonProducerActor(taskType))
}

class CommonProducerActor(taskType: TaskType) extends Producer with Oneway {
  override def endpointUri: String = getCommonEndpointUrlFromTaskType(taskType)
}
