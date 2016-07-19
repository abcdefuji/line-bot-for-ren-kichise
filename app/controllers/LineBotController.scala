package controllers

import config.LineBotConfig
import java.net.URL
import javax.inject._
import play.api.Logger
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.ws.DefaultWSProxyServer
import play.api.libs.ws.WSClient
import play.api.mvc.Action
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import sun.misc.BASE64Encoder
import values.EventType
import values.WordType

@Singleton
class LineBotController @Inject()(ws: WSClient) {

  def echo: Action[JsValue] = Action.async(BodyParsers.parse.json) { data =>

    val content = (data.body \\ "content").head
    val exFrom = (content \ "from").get.as[String]
    val text = WordType.of((content \ "text").get.as[String]).content
    val exText = EventType.of((data.body \\ "eventType").head.as[String]) match {
      case EventType.UserAction => WordType.Introduce.content
      case _ => text
    }

    post(exFrom, exText).map { res =>
      Logger.info(res.body.toString())
      Results.Ok
    }
  }

  private[this] def post(exFrom: String, exText: String) = {

    val body = Json.parse(
      s"""{
          |  "to":["$exFrom"],
          |  "toChannel":1383378250,
          |  "eventType":"138311608800106203",
          |  "content":{
          |    "contentType":1,
          |    "toType":1,
          |    "text":"$exText"
          |  }
          |}
      """.stripMargin)

    val encodedAuth = new BASE64Encoder().encode(
      new URL(LineBotConfig.ProxyURL).getUserInfo.getBytes()
    )

    ws.url("https://trialbot-api.line.me/v1/events")
      .withHeaders(
        "Proxy-Authorization" -> s"Basic $encodedAuth",
        "Content-Type" -> "application/json; charset=UTF-8",
        "X-Line-ChannelID" -> LineBotConfig.ChannelID,
        "X-Line-ChannelSecret" -> LineBotConfig.ChannelSecret,
        "X-Line-Trusted-User-With-ACL" -> LineBotConfig.MID
      )
      .withProxyServer(
        DefaultWSProxyServer(
          host = "velodrome.usefixie.com",
          port = 80,
          principal = Some("fixie"),
          password = Some("gbq3H6Vuqj71gXS")
        )
      ).post(body)
  }
}
