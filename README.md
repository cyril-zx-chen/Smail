# S-Mail
A light-weight Scala library for Email validation.

# Getting Started
A basic example: 


```scala
import cyril.smail.EmailAddress._
import cyril.smail.EmailParseError._

"hi_smail@scala.com".validate

validate("hi_smail@scala.com")
```