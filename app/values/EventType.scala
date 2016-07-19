package values

/**
  * LINE API EventType ENUM
  */
abstract class EventType private(val value: String)

object EventType {
  case object ReceiveMessage extends EventType("138311609000106303")
  case object UserAction extends EventType("138311609100106403")
  case object SendSingleMessage extends EventType("138311608800106203")
  case object SendMultiMessage extends EventType("140177271400161403")
  case class Unknown(override val value: String) extends EventType(value)

  val values =
    Seq(ReceiveMessage, UserAction, SendSingleMessage, SendMultiMessage)


  def of(str: String): EventType = {
    values.find(_.value == str).getOrElse(Unknown(str))
  }
}
