package smail

import cats.syntax.all._
import smail.SmailError._
import eu.timepit.refined.types.string.NonEmptyString

final case class Smail(name: NonEmptyString, domain: NonEmptyString, tld: NonEmptyString)

object Smail {
  private val validName = (name: String) => nameRule.r.matches(name)
  private val validDomain = (name: String) => nameDomain.r.matches(name)
  private val validTld = (name: String) => nameTld.r.matches(name)

  private val nameRule: String = "[a-zA-Z\\d.!#$%&’'*+/=?^_`{|}~-]+"
  private val nameDomain = "[a-zA-Z\\d.-]+"
  private val nameTld = "(?:[a-zA-Z\\d-]+)*"
  private val structure = """^(.+)@(.+)\.(.+)$""".r

  private def toEmailAddress(fullEmail: String): Either[SmailError, Smail] =
    fullEmail match {
      case structure(name, domain, tld) =>
        for {
          n <- (if (validName(name)) NonEmptyString.from(name).leftMap(invalidUserName) else invalidUserName(name).asLeft): Either[SmailError, NonEmptyString]
          d <- (if (validDomain(domain)) NonEmptyString.from(domain).leftMap(invalidDomain) else invalidDomain(domain).asLeft): Either[SmailError, NonEmptyString]
          t <- (if (validTld(tld)) NonEmptyString.from(tld).leftMap(invalidTopLevelDomain) else invalidTopLevelDomain(tld).asLeft): Either[SmailError, NonEmptyString]
        } yield Smail(n, d, t)

      case _ => Left(MissingField(fullEmail))
    }

  def isValid(fullEmail: String): Boolean = toEmailAddress(fullEmail).fold(_ => false, _ => true)

  def from(email: String): Either[SmailError, Smail] = Smail.toEmailAddress(email)

  def unsafeFrom(email: String): Smail = Smail.toEmailAddress(email) match {
    case Left(error) => throw new IllegalArgumentException(s"Unable to parse email: $error")
    case Right(value) => value
  }
}