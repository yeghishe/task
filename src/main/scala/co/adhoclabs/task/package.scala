package co.adhoclabs

import akka.actor.{ ActorRef, ActorSystem }

/**
 * Created by yeghishe on 5/4/15.
 */
package object task {
  case class TaskType(name: String)

  object TaskType {
    val demo = TaskType("demo")
    val inboundSms = TaskType("inbound-sms")
    val outboundSms = TaskType("outbound-sms")
    val slackMessage = TaskType("slack-message")
    val segment = TaskType("segment")

    private val all = Map(
      demo.name -> demo,
      inboundSms.name -> inboundSms,
      outboundSms.name -> outboundSms,
      slackMessage.name -> slackMessage,
      segment.name -> segment
    )
    def fromName(name: String): Option[TaskType] = all.get(name)
  }

  private[task] def getCommonEndpointUrlFromTaskType(taskType: TaskType): String =
    Config.commonEndpointUrl format taskType.name

  def createActors(system: ActorSystem): ActorRef = system.actorOf(TaskActor.props, "task")
}
