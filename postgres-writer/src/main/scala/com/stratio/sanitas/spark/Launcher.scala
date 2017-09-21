package com.stratio.sanitas.spark

import com.stratio.sanitas.spark.io.PostgresConfig
import com.stratio.sanitas.spark.stores.Cliente
import org.apache.spark.deploy.Client
import org.apache.spark.sql.{SaveMode, SparkSession}

import scala.util.{Failure, Success, Try}

/**
  * Created by mariofernandez on 5/09/17.
  */
object Launcher extends App {

  val appName = "spark2.1-jobs-postgres-writer"
  val master = "local"

  val postgresHost: String = extractArg(args, 0).getOrElse("localhost")
  val postgresPort: Int    = extractArg(args, 1).getOrElse("5432").toInt
  val postgresDB: String   = extractArg(args, 2).getOrElse("postgres")
  val postgresUser: String = extractArg(args, 3).getOrElse("postgres")
  val postgresPass: String = extractArg(args, 4).getOrElse("")

  implicit val spark = SparkSession
    .builder()
    .appName(appName)
    .master(master)
    .getOrCreate()


  println(s"Starting application ${spark.conf.get("spark.app.name")}")

  val clientes = spark.read.parquet("/seguros/data/datasets/cliente-seguros/*")

  val conf = PostgresConfig(postgresHost, postgresPort, postgresDB, postgresUser, postgresPass)
  Cliente(conf).write(clientes, Some("clientes"))

  spark.close()
  println("Spark session closed! Ciao!")

  def extractArg(arr: Array[String], index: Int): Option[String]= Try(arr(index)) match {
    case Success(s) => Some(s)
    case Failure(_) => None
  }
}

