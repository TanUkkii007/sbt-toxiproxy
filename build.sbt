name := "sbt-toxiproxy"

sbtPlugin := true

version := "0.0.1-SNAPSHOT"

organization := "github.com/TanUkkii007"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-encoding", "UTF-8",
  "-language:implicitConversions",
  "-language:postfixOps"
)

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))

ScriptedPlugin.scriptedSettings

scriptedBufferLog := false

scriptedLaunchOpts <+= version { "-Dplugin.version=" + _ }

watchSources <++= sourceDirectory map { path => (path ** "*").get }