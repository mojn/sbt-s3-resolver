import sbtrelease._

releaseSettings


sbtPlugin := true

name := "sbt-s3-resolver"

organization := "ohnosequences"

description := "SBT plugin which provides Amazon S3 bucket resolvers"


homepage := Some(url("https://github.com/ohnosequences/sbt-s3-resolver"))

organization := "ohnosequences"

organizationHomepage := Some(url("http://ohnosequences.com"))

licenses += "AGPLv3" -> url("http://www.gnu.org/licenses/agpl-3.0.txt")


scalaVersion := "2.10.2"

crossScalaVersions := Seq("2.9.2", "2.10.2")

crossBuildingSettings

CrossBuilding.crossSbtVersions := Seq("0.12", "0.13")

s3credentialsFile := Some("credentials.properties")

publishMavenStyle := false

//// For publishing set s3credentialsFile (see readme)
publishTo <<= (isSnapshot, s3credentials) { 
                (snapshot,   credentials) => 
  val prefix = if (snapshot) "snapshots" else "releases"
  credentials map S3Resolver(
      "Mojn "+prefix+" publish S3 bucket"
    , "s3://"+prefix + ".public.mojn.com"
    , Resolver.ivyStylePatterns
    ).toSbtResolver
}

resolvers ++= Seq ( 
  "Era7 maven releases"  at "http://releases.era7.com.s3.amazonaws.com"
, "Era7 maven snapshots" at "http://snapshots.era7.com.s3.amazonaws.com"
)

libraryDependencies += "ohnosequences" %% "ivy-s3-resolver" % "0.3.0"
