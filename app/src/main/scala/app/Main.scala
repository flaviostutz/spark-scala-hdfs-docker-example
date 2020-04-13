package app

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.SaveMode

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

object Main extends App {

      val hdfsPrefix = sys.env("HDFS_URL")

      println("Copying sample file to HDFS...")
      val hadoopConf = new Configuration()
      hadoopConf.set("fs.defaultFS", sys.env("HDFS_URL"))
      val hdfs = FileSystem.get(hadoopConf)

      val srcPath = new Path("/app/people.csv")
      val destPath = new Path("hdfs:///test/people.csv")
      hdfs.copyFromLocalFile(srcPath, destPath)
      

      println("Initializing Spark context...")
      // initialise spark context
      val conf = new SparkConf().setAppName("Example App")
      val spark: SparkSession = SparkSession.builder.config(conf).getOrCreate()

      println("Load CSV using Dataframe")
      val df = spark.read
                  .format("com.databricks.spark.csv")
                  .option("inferSchema", "true")
                  .option("header", "true")
                  .load(hdfsPrefix + "/test/people.csv")
      df.describe().show()

      println("Perform some SQL over CSV contents")
      df.createOrReplaceTempView("people")
      val df2 = spark.sql("SELECT * FROM people WHERE Height BETWEEN 68 AND 71")
      df2.describe().show()

      println("Save CSV using Dataframe")
      df2.repartition(5).write
           .format("com.databricks.spark.csv")
           .option("header", "true")
           .mode("overwrite")
           .save(hdfsPrefix + "/test/people-result.csv")

      // do stuff
      println("************")
      println("Hello, world!")
      val rdd = spark.sparkContext.parallelize(Array(1 to 10))
      rdd.count()
      println("************")

      // terminate spark context
      spark.stop()
}

