name := "sbt-toxiproxy"

sbtPlugin := true

organization := "github.com/TanUkkii007"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-encoding", "UTF-8",
  "-language:implicitConversions",
  "-language:postfixOps"
)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

homepage := Some(url("https://github.com/TanUkkii007/sbt-toxiproxy"))

BintrayPlugin.bintraySettings

BintrayPlugin.autoImport.bintrayPackage := "sbt-toxiproxy"

enablePlugins(ReleasePlugin)

ScriptedPlugin.scriptedSettings

scriptedBufferLog := false

scriptedLaunchOpts <+= version { "-Dplugin.version=" + _ }

watchSources <++= sourceDirectory map { path => (path ** "*").get }