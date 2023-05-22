# S-Mail
A light-weight Scala library for Email validation.

# Getting Started
A basic example: 


```scala
import cyril.smail.EmailAddress._
import cyril.smail.EmailParseError._

val validatedWithImplicit: Boolean = "hi_smail@gmail.com".validate

val validatedWithoutImplicit: Boolean = validate("hi_smail@gmail.com")

import cyril.smail.EmailAddress
import cyril.smail.EmailParseError

val validatedWithEither: Either[EmailParseError, EmailAddress] = "hi_smail@scala.com".toEmailAddress
```
With `EmailAddress`, you can get the structured information for each part of the Email, i.e name(hi_smail)/domain(gmail, hotmail, outlook)/tld(com)

`EmailParseError` is a sealed trait with possible pattern matchings:
- `InvalidUserName`: UserName is not empty but is invalid
- `InvalidDomain`: Domain is not empty but is invalid
- `InvalidTopLevelDomain`: TLD is not empty but is invalid
- `MissingField`: any one or more of Username/Domain/TLD is missing