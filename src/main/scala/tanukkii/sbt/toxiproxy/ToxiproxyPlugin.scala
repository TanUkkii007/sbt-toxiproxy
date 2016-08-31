/**
  * Copyright (c) 2015 Graham Rhodes
  *
  * 2016- Modified by Yusuke Yasuda
  *
  * The original source code is at https://github.com/grahamar/sbt-dynamodb/blob/master/src/main/scala/com/teambytes/sbt/dynamodb/DynamoDBLocal.scala
  */

package tanukkii.sbt.toxiproxy

import sbt._
import sbt.Keys._

object ToxiproxyPlugin extends AutoPlugin {

  object Keys {
    val toxiproxyVersion = settingKey[String]("Toxiproxy version to download.")
    val toxiproxyDownloadUrl = settingKey[Option[String]]("toxiProxy URL to download from.")
    val toxiproxyDownloadDirectory = settingKey[File]("The directory toxiProxy will be downloaded to.")
    val toxiproxyHost = settingKey[Option[String]]("host that toxiproxy will use")
    val toxiproxyPort = settingKey[Option[Int]]("The port number that toxiproxy will use")
    val toxiproxyTargetOS = settingKey[String]("The OS Toxiproxy binary is built for. windows, darwin or linux.")
    val toxiproxyTargetArch = taskKey[String]("The architecture Toxiproxy binary is built for. Only amd64 is supported.")
    val deployToxiproxy = taskKey[File]("deploy Toxiproxy")
    val startToxiproxy = taskKey[String]("start Toxiproxy")
    val toxiproxyPid = taskKey[String]("PID of Toxiproxy")
    val stopToxiproxy = taskKey[Unit]("stop Toxiproxy")
  }

  import Keys._

  def settings: Seq[Setting[_]] = Seq(
    toxiproxyVersion := "2.0.0",
    toxiproxyDownloadUrl := None,
    toxiproxyDownloadDirectory := file("toxiproxy"),
    toxiproxyHost := None,
    toxiproxyPort := None,
    toxiproxyTargetOS := {
      val osName= System.getProperty("os.name") match {
        case n: String if !n.isEmpty => n
        case _ => System.getProperty("os")
      }
      if (osName.contains("windows")) "windows"
      else if (osName.contains("Mac OS")) "darwin"
      else "linux"
    },
    toxiproxyTargetArch <<= streams map {
      case (smtramz) =>
        val arch = System.getProperty("os.arch")
        if (!arch.contains("64")) {
          smtramz.log.error(s"$arch architecture is not supported.")
          sys.exit(1)
        }
        "amd64"
    },
    deployToxiproxy <<= (toxiproxyVersion, toxiproxyDownloadUrl, toxiproxyDownloadDirectory, toxiproxyTargetOS, toxiproxyTargetArch, streams) map {
      case (v, url, targetDir, os, arch, streamz) =>
        val log = streamz.log
        val outputFile = new File(targetDir, s"toxiproxy-server-$os-$arch")
        if(!targetDir.exists()){
          log.info(s"Creating Toxiproxy directory $targetDir:")
          targetDir.mkdirs()
        }
        if (!outputFile.exists()) {
          val remoteFile= url.getOrElse(toxiProxyDownloadUrlTemplate(v, os, arch))
          log.info(s"Downloading Toxiproxy from [$remoteFile] to [${outputFile.getAbsolutePath}]")
          (new URL(remoteFile) #> outputFile).!!
          if (!outputFile.canExecute) {
            outputFile.setExecutable(true)
          }
        }
        outputFile
    },
    startToxiproxy <<= (deployToxiproxy, toxiproxyDownloadDirectory, toxiproxyTargetOS, toxiproxyTargetArch, toxiproxyHost, toxiproxyPort) map {
      case (toxiProxy, baseDir, os, arch, host, port) =>
        val hostArg = host.fold(Seq.empty[String])(h => Seq("-host", h))
        val portArg = port.fold(Seq.empty[String])(p => Seq("-port", p.toString))
        val args = Seq(baseDir.getAbsolutePath + s"/toxiproxy-server-$os-$arch") ++ hostArg ++ portArg
        Process(args).run()
        "pid"
    },
    toxiproxyPid <<= streams map {
      case (streamz) =>
        getToxiproxyPid.getOrElse {
          streamz.log.error("Cannot find Toxiproxy PID")
          sys.exit(1)
        }
    },
    stopToxiproxy <<= (toxiproxyPid, toxiproxyTargetOS) map {
      case (pid, os) =>
        if (os == "windows")
          s"Taskkill /PID $pid /F".!
        else
          s"kill $pid".!
    }
  )

  def toxiProxyDownloadUrlTemplate(version: String, os: String, arch: String) = {
    s"https://github.com/Shopify/toxiproxy/releases/download/v$version/toxiproxy-server-$os-$arch"
  }

  private[this] def getToxiproxyPid: Option[String] = Utils.extractPID("ps -c -o command,pid".!!)

}
