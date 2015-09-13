package co.adhoclabs.task.client

import java.io.{ BufferedReader, InputStreamReader }

import co.adhoclabs.model.notifications.PushMessage
import co.adhoclabs.task.Config
import com.urbanairship.api.client.APIClient
import com.urbanairship.api.client.model.{ APIPushResponse, APIClientResponse }
import com.urbanairship.api.push.model.audience.Selectors
import com.urbanairship.api.push.model.notification.Notifications
import com.urbanairship.api.push.model.notification.android.AndroidDevicePayload
import com.urbanairship.api.push.model.{ DeviceType, DeviceTypeData, PushPayload }
import org.apache.http.conn.EofSensorInputStream

import scala.concurrent.{ ExecutionContext, Future }
import scalaz.EitherT
import scalaz.std.anyVal._
import scalaz.std.scalaFuture._
import scalaz.syntax.either._
import scalaz.syntax.equal._
import scalaz.syntax.monad._

trait UAClient extends Config {
  val uaApiClient = APIClient.newBuilder()
    .setKey(uaKey)
    .setSecret(uaSecret)
    .build()

  def send(pushMessage: PushMessage)(implicit ec: ExecutionContext): ClientResponse[Boolean] = {
    def generateAndroidPayload = {
      val builder = AndroidDevicePayload.newBuilder()

      for {
        (key, value) <- pushMessage.extraKeys
      } {
        builder.addExtraEntry(key, value)
      }

      builder
        .setAlert(pushMessage.message)
        .build()
    }

    val notification = Notifications.notification(generateAndroidPayload)

    val payload = PushPayload.newBuilder()
      .setAudience(Selectors.apid(pushMessage.token))
      .setNotification(notification)
      .setDeviceTypes(DeviceTypeData.of(DeviceType.ANDROID))
      .build()

    def futResp: Future[APIClientResponse[APIPushResponse]] = { Future { uaApiClient.push(payload) } }

    EitherT(futResp ∘ { r: APIClientResponse[APIPushResponse] =>
      val status = r.getHttpResponse.getStatusLine.getStatusCode

      if (status ≟ 202) {
        true.right
      } else {
        val respBody = new BufferedReader(
          new InputStreamReader(r.getHttpResponse.getEntity.getContent.asInstanceOf[EofSensorInputStream])
        ).readLine

        (status, respBody).left
      }
    })
  }
}

object UAClient extends UAClient