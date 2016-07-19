package config

/**
  * LINE BOTの環境変数
  */
object LineBotConfig {
  val ProxyURL = sys.env("FIXIE_URL")
  val ChannelID = sys.env("CHANNEL_ID")
  val ChannelSecret = sys.env("CHANNEL_SECRET")
  val MID = sys.env("MID")
}
