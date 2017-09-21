package com.stratio.sanitas.spark.io

import org.apache.spark.sql.{DataFrame, SparkSession}

trait Writer {
  def write(df: DataFrame, target: Option[String] = None)(implicit session: SparkSession)
}
