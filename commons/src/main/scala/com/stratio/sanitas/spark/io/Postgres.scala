package com.stratio.sanitas.spark.io

import com.stratio.sanitas.spark.security.{SSL, SSLSettings}
import com.stratio.sanitas.spark.support.Logging
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

case class PostgresConfig(host: String,
                          port: Int,
                          db: String,
                          username: String,
                          password: String)


trait Postgres {
  this: Writer with Logging with JDBC with SSL =>

  def config: PostgresConfig

  override def extractSSL(implicit session: SparkSession): Option[SSLSettings] = {

    val flowKey = "spark.mesos.driverEnv.SPARK_DATASTORE_SSL_ENABLE"

    if (
      session.sparkContext.getConf.getOption(flowKey).isDefined &&
        session.sparkContext.getConf.get(flowKey) == "true"
    ) {
      log.info("SSL config enabled. Extracting securized connection settings")
      Some(
        Map[String, String](
          "mode"         -> "verify-ca",
          "certPem.path" -> s"${sys.env("SPARK_SSL_CERT_PATH")}/cert.crt",
          "keyPKCS8.path"-> s"${sys.env("SPARK_SSL_CERT_PATH")}/key.pkcs8",
          "caPem.path"   -> s"${sys.env("SPARK_SSL_CERT_PATH")}/caroot.crt"
        )
      )
    } else {
      log.info(s"SSL config not enabled. Did you set ${flowKey} = true?")
      None
    }
  }


  override def write(df: DataFrame, target: Option[String] = None)(implicit session: SparkSession): Unit = {
    log.info(s"Trying to write ${df.count} elements")

    val ssl = extractSSL
    val name = session.conf.get("spark.app.name")

    df.write
      .format("jdbc")
      .option("url", url("postgresql", config.host, config.port, config.db, ssl, config.username, config.password))
      .option("dbtable", target.getOrElse(name))
      .option("driver", "org.postgresql.Driver")
      .mode(SaveMode.Append)
      .save
  }
}
