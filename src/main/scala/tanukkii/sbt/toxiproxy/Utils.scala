/**
  * Copyright (c) 2015 Graham Rhodes
  *
  * 2016- Modified by Yusuke Yasuda
  *
  * The original source code is at https://github.com/grahamar/sbt-dynamodb/blob/master/src/main/scala/com/teambytes/sbt/dynamodb/Utils.scala
  */

package tanukkii.sbt.toxiproxy

private[toxiproxy] object Utils {

  private val pidRegex = """toxiproxy-server \d+""".r

  def extractPID(input: String) = pidRegex.findFirstIn(input).map(_.split(" ")(1))
}
