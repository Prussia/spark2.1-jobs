package com.stratio.sanitas.spark.support

import org.apache.log4j.{Appender, ConsoleAppender, Level, LogManager}

import scala.collection.JavaConverters._

trait Logging {

  @transient lazy protected val log = {
    val logger = LogManager.getLogger(loggerName)
    logger.addAppender(Logging.CONSOLE_APPENDER)
    logger.setLevel(Level.INFO)
    logger.setAdditivity(false)
    logger
  }

  protected def loggerName = this.getClass.getName.stripSuffix("$")

}

object Logging {

  private val CONSOLE_APPENDER = {
    val appender = LogManager.getRootLogger.getAllAppenders.asScala.map(_.asInstanceOf[Appender]).find(_.isInstanceOf[ConsoleAppender])
    appender match {
      case Some(app) => app
      case None => new ConsoleAppender()
    }
  }

}
