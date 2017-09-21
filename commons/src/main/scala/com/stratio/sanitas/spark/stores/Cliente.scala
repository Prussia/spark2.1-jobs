package com.stratio.sanitas.spark.stores

import com.stratio.sanitas.spark.io.{JDBC, Postgres, PostgresConfig, Writer}
import com.stratio.sanitas.spark.security.SSL
import com.stratio.sanitas.spark.support.Logging

/**
  * Created by mariofernandez on 15/09/17.
  */

case class Cliente (private val intConf: PostgresConfig) extends Postgres with Writer with Logging with JDBC with SSL {
  override def config: PostgresConfig = intConf
}
