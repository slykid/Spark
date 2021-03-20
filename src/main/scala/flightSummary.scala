import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object flightSummary {
  def main(args: Array[String]): Unit = {

    // Create Spark Session
    val spark = SparkSession.builder()
      .appName("flightSummary")
      .master("local[*]")
      .config("driver.memory", "4g")
      .config("spark.sql.warehouse", "target/spark-warehouse")
      .getOrCreate()

    // Data load - flight Summary
    val flightSummary = spark.read
        .option("inferSchema", "true")
        .option("header", "true")
        .csv("data/flight-data/csv/2015-summary.csv")

    spark.read
      .option("inferSchema", "true")
      .option("header", "true")
      .csv("data/flight-data/csv/2015-summary.csv")
      .createOrReplaceTempView("flightSummary")

    // Sort Column "count"
//    flightSummary.sort("count").explain()
//    flightSummary.sort("count").take(2)

    // Compare results : Spark Sql vs DataFrame
    spark.sql(
      """
        |select DEST_COUNTRY_NAME, count(1) cnt
        |from flightSummary
        |group by 1
        |order by 2 desc
        |limit 5
        |""".stripMargin).show()

    flightSummary.groupBy("DEST_COUNTRY_NAME").count().limit(5).show()

    spark.sql(
      """
        |select DEST_COUNTRY_NAME, sum(count) as destination_total
        |from flightSummary
        |group by 1
        |order by 2 desc
        |limit 5
        |""".stripMargin).show()

    flightSummary
      .groupBy("DEST_COUNTRY_NAME")
      .sum("count")
      .withColumnRenamed("sum(count)", "destination_total")
      .sort(desc("destination_total"))
      .limit(5)
      .show()

//    sqlResult.explain()
//    dfResult.explain()
//    print(dfResult.show())

    // Close SparkSession & Exit Application
    spark.close()
    System.exit(0)
  }
}
