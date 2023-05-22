package cyril.smail

import cyril.smail.EmailAddress._
import cyril.smail.EmailParseError._
import hedgehog._
import hedgehog.runner._

object EmailAddressSpec extends Properties {
  override def tests: List[Prop] = List(
    property("test implicit", testEmailImplicit),
    property("test validate", testValidate),
    property("test string to internal email class conversion", testToEmailConversion),
    property("test miss field", testMissField),
  )

  private def genEmailElements =
    for {
      name <- Gen.string(Gen.alphaNum, Range.linear(1, 10)).log("name")
      domain <- Gen.string(Gen.alphaNum, Range.linear(1, 10)).log("domain")
      tld <- Gen.string(Gen.alphaNum, Range.linear(1, 10)).log("tld")
    } yield (name, domain, tld)

  def testEmailImplicit: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    val email = s"$n@$d.$t"
    email.toEmailAddress match {
      case Left(_) => Result.failure
      case Right(EmailAddress(name, domain, tld)) => name.value ==== n and domain.value ==== d and tld.value ==== t
    }
  })

  def testValidate: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    Result.assert(EmailAddress.validate(s"$n@$d.$t")) and
      Result.assert(EmailAddress.validate(tuple))
  })

  def testToEmailConversion: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    s"$n@$d.$t".toEmailAddress.fold(_ => Result.failure, _.name.value ==== n) and
      s"$n@$d.$t".toEmailAddress.fold(_ => Result.failure, _.domain.value ==== d) and
      s"$n@$d.$t".toEmailAddress.fold(_ => Result.failure, _.tld.value ==== t)
  })

  def testMissField: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    val emailMissingName = s"@$d.$t"
    val emailMissingDomain = s"$n@.$t"
    val emailMissingTld = s"$n@$d."
    emailMissingName.toEmailAddress.fold(error => error ==== missingField(emailMissingName), _ => Result.failure) and
      emailMissingDomain.toEmailAddress.fold(error => error ==== missingField(emailMissingDomain), _ => Result.failure) and
      emailMissingTld.toEmailAddress.fold(error => error ==== missingField(emailMissingTld), _ => Result.failure)
  })
}
