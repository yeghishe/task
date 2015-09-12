package co.adhoclabs.task.message

object PushMessageTypes extends Enumeration {
  val apns = Value(1, "apns")
  val ua = Value(2, "ua")
  val gcm = Value(3, "gcm")
}

case class PushMessage(token: String,
  pushType: PushMessageTypes.Value,
  message: String,
  badge: Option[Int] = None,
  extraKeys: Map[String, String] = Map.empty,
  category: Option[String] = None)
