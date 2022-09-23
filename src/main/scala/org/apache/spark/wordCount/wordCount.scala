package org.apache.spark.wordCount

import org.apache.spark.sql.SparkSession

object wordCount {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("wordCount")
      .master("local[*]")
      .config("spark.driver.bindAddress", "127.0.0.1")
      .getOrCreate()

    val sparkContext = spark.sparkContext

    val fileRDD = sparkContext.textFile("data/README.md")
    fileRDD.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)
      .foreach(tuple => println(tuple._1, tuple._2))
  }

}
