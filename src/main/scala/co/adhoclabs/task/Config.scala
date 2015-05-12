package co.adhoclabs.task

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
  lazy val producers = taskConfig.getStringList("common-producers").asScala.toList.map(TaskType.fromName(_)
    .getOrElse(throw configError))
  lazy val consumerActorPaths = taskConfig.getObject("common-consumers").unwrapped().asScala.map {
    case (k: String, v: java.util.HashMap[_, _]) ⇒
      TaskType.fromName(k).map(_ -> v.get("target-actor").toString).getOrElse(throw configError)
    case _ ⇒ throw configError
  }.toMap
}

private[task] object Config extends Config
