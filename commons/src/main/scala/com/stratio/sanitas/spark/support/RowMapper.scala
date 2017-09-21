package com.stratio.sanitas.spark.support

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.spark.sql.Row

import scala.collection.JavaConverters._

object RowMapper {

  private val OBJECT_MAPPER = new ObjectMapper()

  def asJson(row:Row) : String = {
    val javaMap = row.getValuesMap(row.schema.fieldNames).asJava
    OBJECT_MAPPER.writeValueAsString(new util.TreeMap[String,Object](javaMap))
  }
}
