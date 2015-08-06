package co.adhoclabs.task.worker

import akka.actor.{ ActorLogging, Actor, Props, Status }
import co.adhoclabs.task.client.SegmentClient
import co.adhoclabs.task.message._

import scalaz.{ \/-, -\/ }

/**
 * Created by yeghishe on 7/13/15.
 */
object SegmentActor {
  def props(segmentClient: SegmentClient): Props = Props(new SegmentActor(segmentClient))
}

class SegmentActor(segmentClient: SegmentClient) extends Actor with ActorLogging {
  import context.dispatcher
  import akka.pattern._

  override def receive: Receive = {
    case e: SegmentOrderEvent ⇒
      log.debug(s"Got $e")
      segmentClient.orderEvent(e).run.pipeTo(self)(sender())
    case \/-(_) ⇒ sender ! OK
    case -\/((_, msg: String)) ⇒ sender ! Error(msg)
    case Status.Failure(e) ⇒ sender ! Error(e.getMessage)
  }
}
