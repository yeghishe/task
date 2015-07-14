package co.adhoclabs.task.client

import co.adhoclabs.task.Config
import co.adhoclabs.task.message.SegmentOrderEvent
import rapture.mime.MimeTypes

import scala.concurrent.{ Future, ExecutionContext }
import scalaz.EitherT
import scalaz.std.anyVal._
import scalaz.std.scalaFuture._
import scalaz.syntax.std.option._
import scalaz.syntax.either._
import scalaz.syntax.equal._
import scalaz.syntax.monad._

/**
 * Created by yeghishe on 7/13/15.
 */
trait SegmentClient extends Config {
  import SegmentClient._
  import rapture._
  import core._
  import io._
  import json._
  import net._
  import rapture.core.timeSystems.numeric
  import rapture.json.jsonBackends.jackson._

  private implicit val JsonPostType = new PostType[Json] {
    def contentType = Some(MimeTypes.`application/json`)
    def sender(content: Json) =
      ByteArrayInput(content.toString().getBytes("UTF-8"))
  }

  def orderEvent(e: SegmentOrderEvent)(implicit ec: ExecutionContext): ClientResponse[Boolean] = {
    val total = e.items.foldRight(BigDecimal(0))((i, sum) ⇒ sum + i.price * i.quantity).toString()
    // FIXME: Figure out how to properly serialize json
    val i = e.items.head
    val j = json"""{
      "userId" : ${e.userId},
      "event" : "Completed Order",
      "properties" : {
        "orderId": ${e.orderId},
        "total": $total,
        "revenue": $total,
        "products": [{
          "id": ${i.sku},
          "sku": ${i.sku},
          "name": ${i.name},
          "price": ${i.price.toString()},
          "quantity": ${i.quantity}
        }]
      }
    }"""

    EitherT(makePostRequest(Https / segmentBaseUrl / "track", j) ∘ { r ⇒
      if (r.status ≟ 200) true.right else (r.status, r.slurp[Char]()).left
    })
  }

  private def makePostRequest(url: HttpUrl, payload: Json)(implicit ec: ExecutionContext): Future[HttpResponse] = {
    import modes.returnFuture
    url.post(payload, authenticate = (segmentWriteKey, "").some, timeout = segmentRequestTimeout)
  }
}

object SegmentClient extends SegmentClient {
  type ClientResponse[R] = EitherT[Future, (Int, String), R]
}
