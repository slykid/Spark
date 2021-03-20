import org.apache.spark.{SparkContext, SparkConf}

object wordcount {
  def main(args: Array[String]) : Unit = {
    val conf = new SparkConf().setAppName("wordCount").setMaster(args(0))
    val sc = new SparkContext(conf)
    val filePath = "data/README.md"
    val inputRDD = sc.textFile(filePath)
    val matchTerm = "spark"

    val numMatches = inputRDD.filter(line => line.contains(matchTerm)).count()
    val lines = inputRDD.count()
    val firstLine = inputRDD.first()

    println("%s lines in %s contains %s".format(numMatches, filePath, matchTerm))
    println("%s lines".format(lines))
    println("%s is first line".format(firstLine))
    System.exit(0)
  }
}