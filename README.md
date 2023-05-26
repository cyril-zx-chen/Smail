# S-Mail
A light-weight Scala library for Email validation.

# Getting Started

## Validate Email

```scala
import smail._

val validate: Boolean = Smail.isValid("hi_smail@gmail.com")
val toSmail: Either[SmailError, Smail] = Smail.from("hi_smail@gmail.com")
val unsafeToSmail: Smail = Smail.unsafeFrom("hi_smail@gmail.com")
```

To enable extension methods, Simply `import smail.syntax._`
```scala
import smail._
import smail.syntax._

val validatedWithSyntax: Boolean = "hi_smail@gmail.com".isValid
val toSmailWithSyntax: Either[SmailError, Smail] = "hi_smail@gmail.com".toSmail
val unsafeToSmailWithSyntax: Smail = "hi_smail@gmail.com".unsafeToSmail
```

## Break Down Email
`class Smail` is protected with refined type NonEmptyString, which means once you get a valid Smail,
you don't need to worry about empty strings in any part of an Email
```scala
import eu.timepit.refined.types.all.NonEmptyString
import smail._
import smail.syntax._

val mySmail: Smail = "hi_smail@gmail.com".unsafeToSmail
val emailName: NonEmptyString = mySmail.name // NonEmptyString("hi_smail") 
val emailDomain: NonEmptyString = mySmail.domain // NonEmptyString("gmail")
val emailTld: NonEmptyString = mySmail.tld // NonEmptyString("com")

val emailNameString: String = mySmail.name.value
val emailDomainString: String = mySmail.domain.value
val emailTldString: String = mySmail.tld.value
```

## Error Handling

With `EmailAddress`, you can get the structured information for each part of the Email, i.e name(hi_smail)/domain(gmail, hotmail, outlook)/tld(com)

`EmailParseError` is a sealed trait with possible pattern matchings:
- `InvalidUserName`: UserName is not empty but is invalid
- `InvalidDomain`: Domain is not empty but is invalid
- `InvalidTopLevelDomain`: TLD is not empty but is invalid
- `MissingField`: any one or more of Username/Domain/TLD is missing