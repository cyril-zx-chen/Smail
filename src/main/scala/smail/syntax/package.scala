package smail

package object syntax {
  implicit class SmailStringOps(email: String) {
    implicit def isValid: Boolean = Smail.isValid(email)

    implicit def toSmail: Either[SmailError, Smail] = Smail.from(email)

    implicit def unsafeToSmail: Smail = Smail.unsafeFrom(email)
  }
}
