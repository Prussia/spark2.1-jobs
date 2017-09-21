
lazy val root = (project in file("."))
  .settings(DefaultSettings.moduleSettings: _*)
  .settings(name := Dependencies.name(Dependencies.projectName, name.value))
  .disablePlugins(AssemblyPlugin)

lazy val commons = (project in file("./commons"))
  .settings(DefaultSettings.moduleSettings: _*)
  .settings(name := Dependencies.name(Dependencies.projectName, name.value))
  .disablePlugins(AssemblyPlugin)

lazy val hdfs_reader = (project in file("./hdfs-reader"))
  .settings(DefaultSettings.moduleSettings: _*)
  .settings(name := Dependencies.name(Dependencies.projectName, name.value))
  .settings(mainClass in assembly := Some("com.stratio.sanitas.spark.Launcher"))
  .dependsOn(commons)

lazy val kafka_writer= (project in file("./kafka-writer"))
  .settings(DefaultSettings.moduleSettings: _*)
  .settings(name := Dependencies.name(Dependencies.projectName, name.value))
  .settings(mainClass in assembly := Some("com.stratio.sanitas.spark.Launcher"))
  .dependsOn(commons)

lazy val postgres_writer = (project in file("./postgres-writer"))
  .settings(DefaultSettings.moduleSettings: _*)
  .settings(name := Dependencies.name(Dependencies.projectName, name.value))
  .settings(mainClass in assembly := Some("com.stratio.sanitas.spark.Launcher"))
  .dependsOn(commons)
