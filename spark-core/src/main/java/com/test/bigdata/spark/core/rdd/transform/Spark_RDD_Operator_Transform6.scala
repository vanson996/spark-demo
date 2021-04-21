package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.{SparkConf, SparkContext}


object Spark_RDD_Operator_Transform6 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Opertor")
    val sparkContext = new SparkContext(sparkConf)

    // todo 算子- key -value  reduceByKey
    // reduceByKey：相同key的数据进行value的聚合操作
    // reduceByKey中的key如果只有一个的话是不会参与运算的
    val rdd = sparkContext.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("b", 4)), 2)

    val value = rdd.reduceByKey((x: Int, y: Int) => x + y)

    value.collect().foreach(println)

    sparkContext.stop()
  }

}
