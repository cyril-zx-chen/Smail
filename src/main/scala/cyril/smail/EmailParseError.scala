package cyril.smail

sealed abstract class EmailParseError(raw: String, message: String)

object EmailParseError {
  case class InvalidUserName(raw: String) extends EmailParseError(raw, s"Invalid user name: $raw")

  case class InvalidDomain(raw: String) extends EmailParseError(raw, s"Invalid domain name: $raw")

  case class InvalidTopLevelDomain(raw: String) extends EmailParseError(raw, s"Invalid TLD name: $raw")

  case class MissingField(raw: String) extends EmailParseError(raw, s"Empty field found: $raw")

  def invalidUserName(raw: String): EmailParseError = InvalidUserName(raw)
  def invalidDomain(raw: String): EmailParseError = InvalidDomain(raw)
  def invalidTopLevelDomain(raw: String): EmailParseError = InvalidTopLevelDomain(raw)
  def missingField(raw: String): EmailParseError = MissingField(raw)
}
