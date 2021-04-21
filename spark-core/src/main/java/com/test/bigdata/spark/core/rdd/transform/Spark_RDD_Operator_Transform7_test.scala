package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 统计各个key的平均值
 */
object Spark_RDD_Operator_Transform7_test {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Opertor")
    val sparkContext = new SparkContext(sparkConf)

    // todo 算子- key -value  aggregate
    // 将数据根据不同规则进行分区内计算和分区间计算
    val rdd = sparkContext.makeRDD(List(("a", 1), ("a", 2), ("b", 3), ("a", 4), ("b", 5), ("b", 6)), 2)

    val value: RDD[(String, (Int, Int))] = rdd.aggregateByKey((0, 0))(
      (x, y) => (x._1 + y, x._2 + 1),
      (x, y) => (x._1 + y._1, x._2 + y._2))

    value.collect().foreach(println)

    value.mapValues {
      case (x, y) => {
        x / y
      }
    }.collect().foreach(println)
    sparkContext.stop()
  }

}
