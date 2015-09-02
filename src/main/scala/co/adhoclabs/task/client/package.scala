package co.adhoclabs.task

import scala.concurrent.Future
import scalaz.EitherT

package object client {
  type ClientResponse[R] = EitherT[Future, (Int, String), R]
}
