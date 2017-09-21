package com.stratio.sanitas.spark.io

import com.stratio.sanitas.spark.security.SSLSettings
import com.stratio.sanitas.spark.support.Logging

trait JDBC {

  this: Logging =>

  private[io] def url(datastore: String,
                      host: String,
                      port: Int,
                      db: String,
                      sslSettings: Option[SSLSettings] = None,
                      username: String,
                      password: String): String = {

    val prefix = s"jdbc:${datastore}://${host}:${port}/${db}"

    val result = sslSettings match {
      case Some(ssl) =>
        s"${prefix}?user=${username}&ssl=true&sslmode=${ssl("mode")}&sslcert=${ssl("certPem.path")}&sslkey=${ssl("keyPKCS8.path")}&sslrootcert=${ssl("caPem.path")}"
      case None =>
        s"${prefix}?user=${username}&password=${password}"
    }


    log.info(s"JDBC url generated: ${result}")

    result
  }
}
