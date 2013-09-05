import sbtrelease._
import ReleaseStateTransformations._
import ReleasePlugin._
import ReleaseKeys._

name := "test-a"

organization := "ohnosequences"

version := "0.0.1"

scalaVersion := "2.10.0"


publishMavenStyle := false

publishTo <<= (isSnapshot, s3credentials, artifactsBucket) {
                (snapshot,   credentials, bucket) =>
  val prefix = "private." + (if (snapshot) "snapshots" else "releases")
  credentials map s3resolver("My "+prefix+" S3 bucket", "s3://"+prefix+"." + "intercrossing.com", Resolver.localBasePattern)
}

resolvers ++= Seq (
                    "Typesafe Releases"   at "http://repo.typesafe.com/typesafe/releases",
                    "Sonatype Releases"   at "https://oss.sonatype.org/content/repositories/releases",
                    "Sonatype Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots",
                    "Era7 Releases"       at "http://releases.era7.com.s3.amazonaws.com",
                    "Era7 Snapshots"      at "http://snapshots.era7.com.s3.amazonaws.com"
                  )

s3credentialsFile in Global := Some("/home/evdokim/ohno.prop")


scalacOptions ++= Seq(
                      "-feature",
                      "-language:higherKinds",
                      "-language:implicitConversions",
                      "-language:postfixOps",
                      "-deprecation",
                      "-unchecked"
                    )

// sbt-release settings

releaseSettings

releaseProcess <<= thisProjectRef apply { ref =>
  Seq[ReleaseStep](
    checkSnapshotDependencies
  , inquireVersions
  , setReleaseVersion
  , publishArtifacts
  , setNextVersion
  )
}