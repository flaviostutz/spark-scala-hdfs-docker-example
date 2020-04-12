package app

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object Main extends App { 
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
