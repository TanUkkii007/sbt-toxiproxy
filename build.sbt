name := "sbt-toxiproxy"

description := "toxiproxy server plugin for sbt project"

sbtPlugin := true

organization := "github.com/TanUkkii007"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-encoding", "UTF-8"
)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/TanUkkii007/sbt-toxiproxy"))

publishMavenStyle := false

bintrayOrganization in bintray := None

bintrayRepository := "sbt-plugin-releases"

bintrayPackage := "sbt-toxiproxy"

enablePlugins(ReleasePlugin)

ScriptedPlugin.scriptedSettings

scriptedBufferLog := false

scriptedLaunchOpts <+= version { "-Dplugin.version=" + _ }

watchSources <++= sourceDirectory map { path => (path ** "*").get }