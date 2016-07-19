package values

/**
  * 吉瀬恋 発言ENUM
  * なんか良いまとめ方ほしい
  */
abstract class WordType private(val content: String, val value: Seq[String])

object WordType {
  case object Introduce
    extends WordType(
      "座右の銘は下克上！！！ 吉瀬 恋です☆ よろしくお願いします♡ https://twitter.com/ren_kichise",
      Seq("自己紹介", "吉瀬恋"))
  case object Twitter
    extends WordType(
      "恋のツイッターです☆ テレビ出演情報やライブの情報を発信しているのでフォローよろしくね♡ https://twitter.com/ren_kichise",
      Seq("Twitter", "ツイッター", "ついったー"))
  case object BackStage
    extends WordType(
      "私が所属するバクステ外神田一丁目のHPです♡ 恋にアイドルポイントいれてもいいよ？笑 https://backst.jp/",
      Seq("バクステ"))
  case object Schedule
    extends WordType(
      "今月のシフトはこちら♡ ぜひ会いに来てね＼(^o^)／ https://twitter.com/ren_kichise/status/749769396348792832",
      Seq("シフト", "出勤日"))
  case class Unknown(override val value: Seq[String])
    extends WordType(
      s"「${value.head}」は対応していないよ！ 以下の言葉を選んでね * 自己紹介 * ツイッター * バクステ * シフト",
      Seq()
    )

  val values =
    Seq(Introduce, Twitter, BackStage, Schedule)


  def of(str: String): WordType =
    values.find(_.value.contains(str)).getOrElse(Unknown(Seq(str)))
}
