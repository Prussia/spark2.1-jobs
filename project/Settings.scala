import sbt._
import sbt.Keys._
import sbtassembly.AssemblyKeys._

trait Settings {
  def moduleSettings: Seq[SettingsDefinition] = Seq (
      organization in ThisBuild := Dependencies.organization,
      version      in ThisBuild := Dependencies.version,
      scalaVersion in ThisBuild := scalaVersionUsed,
      crossPaths   in ThisBuild := Dependencies.crossPaths,
      resolvers                 := commonResolvers,
      libraryDependencies       ++= mainDeps,
      libraryDependencies       ++= testDeps map (_ % "test"),
      test in assembly          := {}
  )

  val scalaVersionUsed = Dependencies.scalaVersion

  def commonResolvers = Dependencies.resolvers

  def mainDeps = Seq.empty[ModuleID]
  def testDeps = Seq.empty[ModuleID]

  def enabledPlugins = Seq.empty[AutoPlugin]
}


object DefaultSettings extends Settings {
  override def mainDeps: Seq[ModuleID] = super.mainDeps ++ Seq(Dependencies.spark, Dependencies.config, Dependencies.kafka, Dependencies.postgres)
}
