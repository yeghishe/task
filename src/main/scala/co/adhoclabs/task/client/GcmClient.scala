package co.adhoclabs.task.client

import co.adhoclabs.task.message.PushMessage
import co.adhoclabs.task.Config
import rapture.mime.MimeTypes

import scala.concurrent.{ ExecutionContext, Future }
import scalaz.EitherT
import scalaz.std.anyVal._
import scalaz.std.scalaFuture._
import scalaz.syntax.either._
import scalaz.syntax.equal._
import scalaz.syntax.monad._

trait GcmClient extends Config {

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

  def send(pushMessage: PushMessage)(implicit ec: ExecutionContext): ClientResponse[Boolean] = {
    val url: HttpUrl = Https / gcmBaseUrl / "gcm" / "send"
    val optionsReqMap = pushMessage.extraKeys

    val jsonString =
      json"""{
      "to": ${pushMessage.token},
      "notification": {
        "title": ${pushMessage.message}
      },
      "data": {
       "com.urbanairship.push.ALERT": ${pushMessage.message},
       "type": ${optionsReqMap("type")},
       "bid": ${optionsReqMap("bid")}
      }
    }
    """

    EitherT(makePostRequest(url, jsonString) ∘ { r ⇒
      if (r.status ≟ 200) true.right else (r.status, r.slurp[Char]()).left
    })
  }

  private def makePostRequest(url: HttpUrl, payload: Json)(implicit ec: ExecutionContext): Future[HttpResponse] = {
    import modes.returnFuture
    url.post(
      payload,
      timeout = gcmRequestTimeout,
      None,
      ignoreInvalidCertificates = true,
      Map("Authorization" -> s"key=$gcmAuthKey")
    )
  }
}

object GcmClient extends GcmClient
