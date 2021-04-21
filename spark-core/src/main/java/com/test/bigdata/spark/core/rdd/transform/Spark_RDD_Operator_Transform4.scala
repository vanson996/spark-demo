package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object Spark_RDD_Operator_Transform4 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Opertor")
    val sparkContext = new SparkContext(sparkConf)

    // todo 算子- glom  将int转换为array
    // 将同一个分区的数据直接转换为相同类型的内存数组进行处理，分区不变
    val rdd = sparkContext.makeRDD(List(1, 2, 3, 4), 2)
    val value: RDD[Array[Int]] = rdd.glom()
    val maxRDD = value.map(num => {
      // 获取分区中的最大值
      num.max
    })
    // 分区最大值求和
    println(maxRDD.collect().sum)

    sparkContext.stop()
  }

}
