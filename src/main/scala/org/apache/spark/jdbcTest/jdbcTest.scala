package org.apache.spark.jdbcTest

import org.apache.spark.sql.SparkSession

object jdbcTest {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .appName("jdbcTest")
      .master("local[*]")
      .config("setBindAddress", "127.0.0.1")
      .getOrCreate()

    println("1. 데이터 로드 from EDB")
    var worksetDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:postgresql://localhost:5432/postgres")
      .option("dbtable", "public.tbokpos_upjong_daily")
      .option("username", "slykid")
      .option("password", "Gallerhead106)")
      .option("driver", "org.postgresql.Driver")  // Maven 프로젝트이므로 pom.xml에 JDBC 드라이버 추가할 것
      .load()

    worksetDF.createOrReplaceTempView("workset")

    println("2. 데이터 확인")
    println(spark.sql("select * from workset").show())

  }
}