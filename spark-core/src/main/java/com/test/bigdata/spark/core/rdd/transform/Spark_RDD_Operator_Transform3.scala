package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 对数据扁平映射
 */
object Spark_RDD_Operator_Transform3 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Opertor")
    val sparkContext = new SparkContext(sparkConf)

    // 算子- flatmap 将数据进行扁平映射
    val rdd = sparkContext.makeRDD(List(List(1, 2), List(4, 5, 6)))
    val value: RDD[Int] = rdd.flatMap(list => {
      list
    })
    value.collect().foreach(println(_))

    sparkContext.stop()
  }

}
