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

@Singleton
class LineBotController @Inject()(ws: WSClient) {

  def echo: Action[JsValue] = Action.async(BodyParsers.parse.json) { data =>

    val content = (data.body \\ "content").head
    val exFrom = (content \ "from").get.as[String]
    val exText = (content \ "text").get.as[String]

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
      ).post(body).map { res =>
      Logger.info(res.body.toString())
      Results.Ok
    }
  }
}

case class LineMessage(
  from: String,
  fromChannel: String,
  to: String,
  toChannel: String,
  eventType: EventType,
  id: String,
  content: String
)

case class EventType private(value: String)

object EventType {
  val ReceiveMessage = EventType("138311609000106303")
  val UserAction = EventType("138311609100106403")
  val SendSingleMessage = EventType("138311608800106203")
  val SendMultiMessage = EventType("140177271400161403")
}

/**
class App < Sinatra::Base
  post '/linebot/callback' do
    params = JSON.parse(request.body.read)

    params['result'].each do |msg|
      request_content = {
        to: [msg['content']['from']],
        toChannel: 1383378250, # Fixed  value
        eventType: "138311608800106203", # Fixed value
        content: msg['content']
      }

      endpoint_uri = 'https://trialbot-api.line.me/v1/events'
      content_json = request_content.to_json

      RestClient.proxy = ENV["FIXIE_URL"]
      RestClient.post(endpoint_uri, content_json, {
        'Content-Type' => 'application/json; charset=UTF-8',
        'X-Line-ChannelID' => ENV["LINE_CHANNEL_ID"],
        'X-Line-ChannelSecret' => ENV["LINE_CHANNEL_SECRET"],
        'X-Line-Trusted-User-With-ACL' => ENV["LINE_CHANNEL_MID"],
      })
    end

    "OK"
  end
end
  */
