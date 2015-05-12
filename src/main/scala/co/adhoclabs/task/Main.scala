package co.adhoclabs.task

import akka.actor.ActorSystem
import co.adhoclabs.task.demo.{ DemoSenderActor, DemoReceiverActor }

/**
 * Created by yeghishe on 3/3/15.
 */
object Main extends App {
  val system = ActorSystem("task-system")

  val taskActor = createActors(system)
  system.actorOf(DemoReceiverActor.props, "receiver-demo")
  system.actorOf(DemoSenderActor.props(taskActor), "sender-demo")
}
