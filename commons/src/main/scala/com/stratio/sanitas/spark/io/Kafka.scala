package com.stratio.sanitas.spark.io

import com.stratio.sanitas.spark.security.{SSL, SSLSettings}
import com.stratio.sanitas.spark.support.{Logging, RowMapper}
import com.typesafe.config.Config
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}

import scala.collection.JavaConverters._

class KafkaConfig(config: Config) {
  val brokers       = config.getStringList("brokers").asScala.mkString(",")
  val topic         = config.getString("topic")
}

trait Kafka {

  this: Writer with Logging with SSL =>

  def kafkaConfig: KafkaConfig

  override def extractSSL(implicit session: SparkSession): Option[SSLSettings] = {
    val prefixKafka = "spark.ssl.kafka."

    if (
      session.sparkContext.getConf.getOption(prefixKafka + "enabled").isDefined &&
        session.sparkContext.getConf.get(prefixKafka + "enabled") == "true"
    ) {

      log.info("SSL config enabled. Extracting securized kafka connection settings")
      val configKafka = session.sparkContext.getConf.getAllWithPrefix(prefixKafka).toMap

      Some(
        Map[String, String](
          "security.protocol" -> "SSL",
          "ssl.key.password" -> configKafka("keyPassword"),
          "ssl.keystore.location" -> configKafka("keyStore"),
          "ssl.keystore.password"-> configKafka("keyStorePassword"),
          "ssl.truststore.location"-> configKafka("trustStore"),
          "ssl.truststore.password"-> configKafka("trustStorePassword")
        )
      )

    } else {
      log.info(s"SSL config not enabled. Did you set ${prefixKafka}enabled = true?")
      None
    }
  }

  private val INTEGER_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.IntegerSerializer"
  private val STRING_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer"

  def createRecord(row:Row) : String = RowMapper.asJson(row)

  private def kafkaProducer(ssl: Option[SSLSettings] = None): KafkaProducer[Int, String] = {
    val config = kafkaConfig
    val props = new java.util.HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.brokers)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, INTEGER_SERIALIZER_CLASS)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, STRING_SERIALIZER_CLASS)
    ssl match {
      case Some(m) => props.putAll(m.asJava)
      case _ => {}
    }
    new KafkaProducer[Int, String](props)
  }


  override def write(df: DataFrame, target: Option[String] = None)(implicit session: SparkSession): Unit = {
    val count = df.count
    if (count > 0) {
      val sslSettings = extractSSL
      df.rdd.foreachPartition(partition => {
        val topic = target.getOrElse(kafkaConfig.topic)
        val producer = kafkaProducer(sslSettings)

        partition.zipWithIndex.foreach {
          case (row, index) => {
            val value = createRecord(row)
            log.info(s"Sending to kafka [${topic}, ${index}, ${value}]")
            val message = new ProducerRecord[Int, String](topic, index, value)
            producer.send(message)
          }
        }
        producer.flush()
        producer.close()
      })
    }
    log.info(s"Info sent to kafka [${kafkaConfig.topic}, ${df.count}]")
  }
}
