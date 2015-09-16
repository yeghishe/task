package co.adhoclabs.task.worker

import akka.actor.{ Actor, ActorLogging, Props, Status }
import akka.camel.CamelExtension
import co.adhoclabs.model.notifications.{ PushMessageTypes, PushMessage }
import co.adhoclabs.task.client.{ GcmClient, UAClient }
import co.adhoclabs.task.message._
import org.apache.camel.component.apns.ApnsComponent
import org.apache.camel.component.apns.factory.ApnsServiceFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scalaz.{ -\/, \/- }

object PushActor {
  def props(uaClient: UAClient,
    gcmClient: GcmClient,
    apnsCertificatePath: Option[String],
    apnsCertificatePwd: Option[String]): Props =
    Props(new PushActor(uaClient, gcmClient, apnsCertificatePath, apnsCertificatePwd))
}

class PushActor(uaClient: UAClient,
    gcmClient: GcmClient,
    apnsCertificatePath: Option[String],
    apnsCertificatePwd: Option[String]) extends Actor with ActorLogging {
  val camel = CamelExtension(context.system)
  val camelContext = camel.context

  private def configureApns() {
    val apnsServiceFactory = new ApnsServiceFactory(camelContext)
    apnsCertificatePath.foreach(apnsServiceFactory.setCertificatePath)
    apnsCertificatePwd.foreach(apnsServiceFactory.setCertificatePassword)
    val apnsService = apnsServiceFactory.getApnsService
    val apnsComponent = camelContext.getComponent("apns", classOf[ApnsComponent])
    apnsComponent.setApnsService(apnsService)
  }

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    for {
      certPath <- apnsCertificatePath
      certPwd <- apnsCertificatePwd
    } {
      val apnsServiceFactory = new ApnsServiceFactory(camelContext)
      apnsServiceFactory.setCertificatePath(certPath)
      apnsServiceFactory.setCertificatePassword(certPwd)
      val apnsService = apnsServiceFactory.getApnsService
      val apnsComponent = camelContext.getComponent("apns", classOf[ApnsComponent])
      apnsComponent.setApnsService(apnsService)
    }
  }

  override def receive: Receive = {
    case msg: PushMessage if msg.pushType == PushMessageTypes.apns =>
      import rapture._
      import json._
      import rapture.json.jsonBackends.jackson._

      val empty = json"""{}"""
      val jsonBadge = msg.badge.fold(empty)(b ⇒ json"""{"aps":{ "badge":$b }}""")
      val jsonCategory = msg.category.fold(empty)(b ⇒ json"""{"aps":{ "category":$b }}""")
      val jsonExtras = json"""${msg.extraKeys}"""
      val jsonString = json"""
        {
         "aps": {
           "alert": {
             "body": ${msg.message}
           },
           "sound": "default"
          }
        }""" ++ jsonExtras ++ jsonBadge ++ jsonCategory

      val producerTemplate = camel.template
      producerTemplate.sendBodyAndHeader(
        "apns:notify?tokens=" + msg.token,
        jsonString.toString(),
        "CamelApnsMessageType", "PAYLOAD"
      )
      sender ! OK

    case msg: PushMessage if msg.pushType == PushMessageTypes.ua =>
      import akka.pattern._
      uaClient.send(msg).run.pipeTo(self)(sender())

    case msg: PushMessage if msg.pushType == PushMessageTypes.gcm =>
      import akka.pattern._

      gcmClient.send(msg).run.pipeTo(self)(sender())

    case \/-(_) ⇒
      sender ! OK
    case -\/((_, msg: String)) ⇒
      sender ! Error(msg)
    case Status.Failure(e) ⇒
      sender ! Error(e.getMessage)
  }
}