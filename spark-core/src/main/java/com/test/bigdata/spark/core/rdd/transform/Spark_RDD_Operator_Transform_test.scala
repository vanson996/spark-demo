package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 获取每个分区中的最大值
 */
object Spark_RDD_Operator_Transform_test {
  def main(args: Array[String]): Unit = {
    val sc = new SparkConf().setMaster("local[*]").setAppName("Spark_RDD_Operator_Transform_test")
    val sparkContext = new SparkContext(sc)
    // 设置两个分区
    val rdd = sparkContext.makeRDD(List(1, 2, 3, 4), 2)
    val value = rdd.mapPartitions(iter => {
      List(iter.max).iterator
    })
    value.collect().foreach(println)

    sparkContext.stop()

  }

}
