package com.stratio.sanitas.spark

import com.stratio.sanitas.spark.stores.AlertasCliente
import org.apache.spark.sql.SparkSession

/**
  * Created by mariofernandez on 5/09/17.
  */
object Launcher extends App {

  val appName = "spark2.1-jobs-kafka-writer"
  val master = "local"

  implicit val spark = SparkSession
    .builder()
    .appName(appName)
    .master(master)
    .getOrCreate()


  println(s"Starting application ${spark.conf.get("spark.app.name")}")


  val df = spark.read.parquet("/seguros/data/datasets/cliente-seguros/*")

  AlertasCliente.write(df)

  spark.close()

  println("Spark session closed! Ciao!")
}
