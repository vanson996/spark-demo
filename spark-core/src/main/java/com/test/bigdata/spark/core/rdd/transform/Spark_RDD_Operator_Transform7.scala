package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.{SparkConf, SparkContext}


object Spark_RDD_Operator_Transform7 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Opertor")
    val sparkContext = new SparkContext(sparkConf)

    // todo 算子- key -value  aggregate
    // 将数据根据不同规则进行分区内计算和分区间计算
    val rdd = sparkContext.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("a", 4)), 2)

    // 第一个参数 零值：表示初始值，主要用于当碰见第一个key时，和value进行分区内计算
    // 第二个参数，需要传递两个计算规则，第一个用于分区内计算，第二个用于分区间计算
    rdd.aggregateByKey(0)((x, y) => math.max(x, y), (x, y) => x + y).collect().foreach(println)

    sparkContext.stop()
  }

}
