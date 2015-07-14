package co.adhoclabs.task.producer

import akka.actor.Props
import co.adhoclabs.task._

/**
 * Created by yeghishe on 5/4/15.
 */
object CommonProducerActor {
  def props(taskType: TaskType): Props = Props(new CommonProducerActor(taskType))
}

class CommonProducerActor(override val taskType: TaskType) extends BaseProducer
