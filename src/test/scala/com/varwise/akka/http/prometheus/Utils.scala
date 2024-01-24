package com.varwise.pekko.http.prometheus

import scala.util.Random

object Utils {
  def generateRandomString: String = generateRandomStringOfLength(16)

  def generateRandomStringOfLength(length: Int): String =
    Random.alphanumeric.filter(_.isLetter).take(length).mkString("")
}
