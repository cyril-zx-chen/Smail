package smail

import eu.timepit.refined.types.string.NonEmptyString
import hedgehog._
import hedgehog.runner._

object SmailSpec extends Properties {
  override def tests: List[Prop] = List(
    property("test isValid", testValidateEmail),
    property("test 'from' method, string to Smail", testToSmailFromString),
    property("test 'unSafeFrom' method, string to Smail", testToSmailFromString),
    property("test miss field", testMissField),
  )

  private def genEmailElements =
    for {
      name <- Gen.string(Gen.alphaNum, Range.linear(1, 10)).log("name")
      domain <- Gen.string(Gen.alphaNum, Range.linear(1, 10)).log("domain")
      tld <- Gen.string(Gen.alphaNum, Range.linear(1, 10)).log("tld")
    } yield (name, domain, tld)

  def testValidateEmail: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    Result.assert(Smail.isValid(s"$n@$d.$t"))
  })

  def testToSmailFromString: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    Smail.from(s"$n@$d.$t").fold(_ => Result.failure, _.name.value ==== n) and
      Smail.from(s"$n@$d.$t").fold(_ => Result.failure, _.domain.value ==== d) and
      Smail.from(s"$n@$d.$t").fold(_ => Result.failure, _.tld.value ==== t)
  })

  def testToSmailUnsafeFromString: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    Smail.unsafeFrom(s"$n@$d.$t") ==== Smail(NonEmptyString.unsafeFrom(n), NonEmptyString.unsafeFrom(d), NonEmptyString.unsafeFrom(t))
  })

  def testMissField: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    val emailMissingName = s"@$d.$t"
    val emailMissingDomain = s"$n@.$t"
    val emailMissingTld = s"$n@$d."
    Smail.from(emailMissingName).fold(error => error ==== SmailError.missingField(emailMissingName), _ => Result.failure) and
      Smail.from(emailMissingDomain).fold(error => error ==== SmailError.missingField(emailMissingDomain), _ => Result.failure) and
      Smail.from(emailMissingTld).fold(error => error ==== SmailError.missingField(emailMissingTld), _ => Result.failure)
  })
}
