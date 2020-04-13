package app

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path

object Main extends App {

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

      // do stuff
      println("************")
      println("************")
      println("Hello, world!")
      val rdd = spark.sparkContext.parallelize(Array(1 to 10))
      rdd.count()
      println("************")
      println("************")

      // terminate spark context
      spark.stop()
}
