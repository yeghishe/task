package co.adhoclabs

import akka.actor.{ ActorRef, ActorSystem }

/**
 * Created by yeghishe on 5/4/15.
 */
package object task {
  case class TaskType(name: String)

  object TaskType {
    val demo = TaskType("demo")

    private val all = Map(
      demo.name -> demo
    )
    def fromName(name: String): Option[TaskType] = all.get(name)
  }

  private[task] def getCommonEndpointUrlFromTaskType(taskType: TaskType): String =
    Config.commonEndpointUrl format taskType.name

  def createActors(system: ActorSystem): ActorRef = system.actorOf(TaskActor.props, "task")
}
