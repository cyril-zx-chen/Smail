package smail

sealed abstract class SmailError(raw: String, message: String)

object SmailError {
  case class InvalidUserName(raw: String) extends SmailError(raw, s"Invalid user name: $raw")

  case class InvalidDomain(raw: String) extends SmailError(raw, s"Invalid domain name: $raw")

  case class InvalidTopLevelDomain(raw: String) extends SmailError(raw, s"Invalid TLD name: $raw")

  case class MissingField(raw: String) extends SmailError(raw, s"Empty field found: $raw")

  def invalidUserName(raw: String): SmailError = InvalidUserName(raw)
  def invalidDomain(raw: String): SmailError = InvalidDomain(raw)
  def invalidTopLevelDomain(raw: String): SmailError = InvalidTopLevelDomain(raw)
  def missingField(raw: String): SmailError = MissingField(raw)
}
