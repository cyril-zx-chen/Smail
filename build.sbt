ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

wartremoverErrors ++= Warts.unsafe

val refinedVersion = "0.10.1"
val hedgehogVersion = "0.10.0"
val catsVersion = "2.9.0"
val catsEffectVersion = "3.4.7"

lazy val smail = (project in file("."))
  .settings(
    name := "Smail",
    libraryDependencies ++= Seq(
      "eu.timepit" %% "refined" % refinedVersion,
    ) ++ hedgehog ++ cats
  )

val hedgehog = Seq(
  "qa.hedgehog" %% "hedgehog-core" % hedgehogVersion,
  "qa.hedgehog" %% "hedgehog-runner" % hedgehogVersion,
  "qa.hedgehog" %% "hedgehog-sbt" % hedgehogVersion,
).map(_ % Test)

val cats = Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
)