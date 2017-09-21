import sbt._

object Dependencies {
  lazy val organization = "com.stratio"
  lazy val version      = "0.0.1-SNAPSHOT"
  lazy val projectName  = "spark2.1-jobs"
  lazy val scalaVersion = "2.11.8"
  lazy val resolvers    = Seq.empty[Resolver]
  lazy val crossPaths  = false

  /* Spark */
  lazy val spark: ModuleID     = "org.apache.spark" %% "spark-sql" % "2.1.0" % "provided"
  lazy val config: ModuleID    = "com.typesafe" % "config" % "1.3.1"
  lazy val kafka: ModuleID     = "org.apache.kafka" % "kafka-clients" % "0.11.0.1"
  lazy val postgres: ModuleID  = "org.postgresql" % "postgresql" % "42.1.4"




  def name(prefix: String, name: String): String = s"${prefix}-${name}"
}
