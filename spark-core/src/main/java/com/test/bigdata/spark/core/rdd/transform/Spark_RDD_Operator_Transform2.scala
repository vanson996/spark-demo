package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

/**
 * 对不同分区进行操作
 */
object Spark_RDD_Operator_Transform2 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Opertor")
    val sparkContext = new SparkContext(sparkConf)

    // 算子- mapPartitionsWithIndex 可传入分区数据，对分区进行操作
    // mapPartitionsWithIndex：将待处理的数据以分区为单位发送到计算节点进行处理，这里的处理是指可以进行任意的处
    //理，哪怕是过滤数据，在处理时同时可以获取当前分区索引。
    val rdd = sparkContext.makeRDD(List(1, 2, 3, 4), 2)
    val value = rdd.mapPartitionsWithIndex((index, iter) => {
      if (index == 1) {
        iter
      } else {
        Nil.iterator
      }
    })
    value.collect().foreach(println(_))
    sparkContext.stop()
  }
}
