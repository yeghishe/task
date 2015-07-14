package co.adhoclabs.task

/**
 * Created by yeghishe on 8/3/15.
 */
package object message {
  case object OK
  case class Error(msg: String)
}
