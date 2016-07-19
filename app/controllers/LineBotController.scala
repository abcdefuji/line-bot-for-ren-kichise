package controllers

import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.Results

class LineBotController {

  def echo: Action[AnyContent] = Action { request =>
    println(Json.prettyPrint(request.body.asJson.get))
    Results.Ok(views.html.index())
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

case class EventType private (value: String)
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
