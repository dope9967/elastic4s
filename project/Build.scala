import com.typesafe.sbt.SbtPgp
import com.typesafe.sbt.pgp.PgpKeys
import sbt._
import sbt.plugins.JvmPlugin
import sbt.Keys._

object Build extends AutoPlugin {

  override def trigger = AllRequirements
  override def requires = JvmPlugin

  object autoImport {
    val org = "com.sksamuel.elastic4s"
    val AkkaVersion = "2.5.13"
    val CatsVersion = "1.4.0"
    val CirceVersion = "0.9.3"
    val CommonsIoVersion = "2.4"
    val ElasticsearchVersion = "6.4.0"
    val ExtsVersion = "1.60.0"
    val JacksonVersion = "2.10.0"
    val Json4sVersion = "3.6.1"
    val SprayJsonVersion = "1.3.4"
    val AWSJavaSdkVersion = "1.11.342"
    val Log4jVersion = "2.9.1"
    val MockitoVersion = "1.9.5"
    val PlayJsonVersion = "2.6.9"
    val ReactiveStreamsVersion = "1.0.2"
    val ScalatestVersion = "3.0.5"
    val Slf4jVersion = "1.7.25"
  }

  import autoImport._

  override def projectSettings = Seq(
    organization := org,
    scalaVersion := "2.12.6",
    crossScalaVersions := Seq("2.11.12", "2.12.6"),
    publishMavenStyle := true,
    resolvers += Resolver.mavenLocal,
    resolvers += Resolver.url("https://artifacts.elastic.co/maven"),
    javaOptions ++= Seq(
      "-Xms512M",
      "-Xmx2048M",
      "-XX:MaxPermSize=2048M",
      "-XX:+CMSClassUnloadingEnabled"
    ),
    publishArtifact in Test := false,
    fork in Test := false,
    parallelExecution in ThisBuild := false,
    SbtPgp.autoImport.useGpg := true,
    SbtPgp.autoImport.useGpgAgent := true,
    sbtrelease.ReleasePlugin.autoImport.releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    sbtrelease.ReleasePlugin.autoImport.releaseCrossBuild := true,
    credentials += Credentials(Path.userHome / ".sbt" / "pgp.credentials"),
    scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    javacOptions := Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Seq(
      "com.sksamuel.exts" %% "exts" % ExtsVersion,
      "org.slf4j" % "slf4j-api" % Slf4jVersion,
      "org.mockito" % "mockito-all" % MockitoVersion % "test",
      "org.scalatest" %% "scalatest" % ScalatestVersion % "test"
    ),
    publishTo := Some(
      "GitHub dope9967 Apache Maven Packages" at "https://maven.pkg.github.com/dope9967/elastic4s"
    ),
    credentials += Credentials(
      "GitHub Package Registry",
      "maven.pkg.github.com",
      "dope9967",
      sys.env.getOrElse(
        "GITHUB_TOKEN",
        throw new IllegalAccessException("Missing GITHUB_TOKEN")
      )
    )
  )
}
