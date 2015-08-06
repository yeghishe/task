package co.adhoclabs.task.demo

import akka.actor.{ Actor, ActorLogging, Props }
import co.adhoclabs.task.message.OK

/**
 * Created by yeghishe on 5/4/15.
 */
object DemoReceiverActor {
  def props: Props = Props(new DemoReceiverActor)
}

class DemoReceiverActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case DemoPayload(i) if i == 2 ⇒
    case msg @ DemoPayload(i) if i == 4 ⇒
      log.debug(s"throwing exception for $msg")
      throw new RuntimeException("DemoReceiverActor failure")
    case msg @ DemoPayload(i) ⇒
      log.debug(s"responding with OK to $msg")
      sender() ! OK
  }
}
