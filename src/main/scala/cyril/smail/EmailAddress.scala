package cyril.smail

import cats.syntax.all._
import cyril.smail.EmailParseError._
import eu.timepit.refined.types.string.NonEmptyString

final case class EmailAddress(name: NonEmptyString, domain: NonEmptyString, tld: NonEmptyString)

object EmailAddress {
  private val validName = (name: String) => nameRule.r.matches(name)
  private val validDomain = (name: String) => nameDomain.r.matches(name)
  private val validTld = (name: String) => nameTld.r.matches(name)

  private val nameRule: String = "[a-zA-Z\\d.!#$%&’'*+/=?^_`{|}~-]+"
  private val nameDomain = "[a-zA-Z\\d.-]+"
  private val nameTld = "(?:[a-zA-Z\\d-]+)*"
  private val structure = """^(.+)@(.+)\.(.+)$""".r

  def toEmailAddress(fullEmail: String): Either[EmailParseError, EmailAddress] =
    fullEmail match {
      case structure(name, domain, tld) =>
        for {
          n <- (if (validName(name)) NonEmptyString.from(name).leftMap(invalidUserName) else invalidUserName(name).asLeft) : Either[EmailParseError, NonEmptyString]
          d <- (if (validDomain(domain)) NonEmptyString.from(domain).leftMap(invalidDomain) else invalidDomain(domain).asLeft) : Either[EmailParseError, NonEmptyString]
          t <- (if (validTld(tld)) NonEmptyString.from(tld).leftMap(invalidTopLevelDomain) else invalidTopLevelDomain(tld).asLeft) : Either[EmailParseError, NonEmptyString]
        } yield EmailAddress(n, d, t)

      case _ => Left(MissingField(fullEmail))
    }

  def validate(fullEmail: String): Boolean = toEmailAddress(fullEmail).fold(_ => false, _ => true)

  def validate(nameDomainTld: (String, String, String)): Boolean =
    validName(nameDomainTld._1) && validDomain(nameDomainTld._2) && validTld(nameDomainTld._3)

  implicit class EmailAddressEnrich(email: String) {
    implicit def validate: Boolean = EmailAddress.validate(email)

    implicit def toEmailAddress: Either[EmailParseError, EmailAddress] = EmailAddress.toEmailAddress(email)
  }
}