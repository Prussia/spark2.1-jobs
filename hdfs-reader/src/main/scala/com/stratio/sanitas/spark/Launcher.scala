package com.stratio.sanitas.spark

import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * Created by mariofernandez on 5/09/17.
  */
object Launcher extends App {

  val appName = "spark2.1-jobs-hdfs-reader"
  val master = "local"
  val csvFilename = args(0)
  val parquetPath = args(1)

  val spark = SparkSession
    .builder()
    .appName(appName)
    .master(master)
    .getOrCreate()


  println(s"Starting application ${spark.conf.get("spark.app.name")}")

  val df = spark.read.format("csv")
    .option("header", true)
    .option("delimiter", "|")
    .option("mode", "FAILFAST")
    .option("inferSchema", true)
    .option("comment", "#")
    .load(csvFilename)


  df.write.partitionBy("id_sesion").mode(SaveMode.Append).parquet(parquetPath)
  spark.close()

  println("Spark session closed! Ciao!")
}
