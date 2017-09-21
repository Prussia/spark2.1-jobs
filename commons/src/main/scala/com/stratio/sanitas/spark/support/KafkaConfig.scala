package com.stratio.sanitas.spark.support

import java.io.File

import com.stratio.sanitas.spark.io.KafkaConfig
import com.typesafe.config.{Config, ConfigFactory}

class ConfigBuilder (private val config : Config) {

  private val NAMESPACE = "jobs"

  def getConfig(key: String) = config.getConfig(s"$NAMESPACE.$key")

  def kafka(key:String) = new KafkaConfig(getConfig(key))
}

object ConfigBuilder {

  val CONFIG = new ConfigBuilder(
    ConfigFactory
      .parseFile(new File("kafka.conf"))
      .withFallback(ConfigFactory.load()))

  def apply() : ConfigBuilder= CONFIG

}