package co.adhoclabs.task

import co.adhoclabs.task.worker.SegmentActor
import co.adhoclabs.task.client.SegmentClient
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConverters._
import scala.concurrent.duration._

/**
 * Created by yeghishe on 5/4/15.
 */
private[task] trait Config {
  private val configError = new RuntimeException("Bad configuration")
  private lazy val taskConfig = ConfigFactory.load().getConfig("task")
  lazy val commonEndpointUrl = taskConfig.getString("common-endpoint-url")
  lazy val targetActorTimeout = Duration(taskConfig.getDuration("target-actor-timeout", SECONDS), SECONDS)
  lazy val producers = taskTypesFromKey("producers")
  lazy val consumers = taskTypesFromKey("consumers")
  lazy val consumerProxyActors = taskConfig.getObject("consumer-proxy-actors").unwrapped().asScala.map {
    case (k: String, v: java.util.HashMap[_, _]) ⇒
      TaskType.fromName(k).map(_ -> v.get("target-actor").toString).getOrElse(throw configError)
    case _ ⇒ throw configError
  }.toMap

  private val segmentConfig = taskConfig.getConfig("segment")
  val segmentWriteKey = segmentConfig.getString("write-key")
  val segmentBaseUrl = segmentConfig.getString("base-url")
  val segmentRequestTimeout = segmentConfig.getDuration("request-timeout", MILLISECONDS)

  private def taskTypesFromKey(key: String): List[TaskType] =
    taskConfig.getStringList(key).asScala.toList.map(TaskType.fromName(_).getOrElse(throw configError))

  val workerActorPropMap = Map(
    TaskType.segment -> SegmentActor.props(SegmentClient)
  )
}

private[task] object Config extends Config
