package smail.syntax

import eu.timepit.refined.types.string.NonEmptyString
import hedgehog._
import hedgehog.runner._
import smail._

object SmailImplicitSpec extends Properties {
  override def tests: List[Prop] = List(
    property("test implicit isValid", testIsValidImplicit),
    property("test implicit toSmail", testToSmailImplicit),
    property("test implicit unsafeToSmail", testToUnsafeSmailImplicit),
  )

  private def genEmailElements =
    for {
      name <- Gen.string(Gen.alphaNum, Range.linear(1, 10)).log("name")
      domain <- Gen.string(Gen.alphaNum, Range.linear(1, 10)).log("domain")
      tld <- Gen.string(Gen.alphaNum, Range.linear(1, 10)).log("tld")
    } yield (name, domain, tld)

  def testIsValidImplicit: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    val email = s"$n@$d.$t"
    email.isValid ==== true
  })

  def testToSmailImplicit: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    val email = s"$n@$d.$t"
    email.toSmail match {
      case Left(_) => Result.failure
      case Right(Smail(name, domain, tld)) => name.value ==== n and domain.value ==== d and tld.value ==== t
    }
  })

  def testToUnsafeSmailImplicit: Property = genEmailElements.map(tuple => {
    val (n, d, t) = tuple
    val email = s"$n@$d.$t"
    email.unsafeToSmail ==== Smail(NonEmptyString.unsafeFrom(n), NonEmptyString.unsafeFrom(d), NonEmptyString.unsafeFrom(t))
  })
}
