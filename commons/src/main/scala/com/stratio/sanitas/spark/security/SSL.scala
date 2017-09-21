package com.stratio.sanitas.spark.security

import org.apache.spark.sql.SparkSession

trait SSL {
  def extractSSL(implicit session: SparkSession): Option[SSLSettings]
}

