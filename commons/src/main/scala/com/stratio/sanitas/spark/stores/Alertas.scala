package com.stratio.sanitas.spark.stores

import com.stratio.sanitas.spark.io.{Kafka, KafkaConfig, Writer}
import com.stratio.sanitas.spark.security.SSL
import com.stratio.sanitas.spark.support.{ConfigBuilder, Logging}

/**
  * Created by mariofernandez on 13/09/17.
  */
case object AlertasCliente extends Kafka with Writer with Logging with SSL {
  override def kafkaConfig: KafkaConfig = ConfigBuilder().kafka("alertas.cliente")
}
