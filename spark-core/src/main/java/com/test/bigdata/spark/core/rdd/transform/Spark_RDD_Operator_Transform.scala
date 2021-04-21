package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.{SparkConf, SparkContext}

object Spark_RDD_Operator_Transform {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Opertor")
    val sparkContext = new SparkContext(sparkConf)
    val rdd = sparkContext.makeRDD(List(1, 2, 3, 4))


    // 转换函数
    def mapFunction(mun: Int): Int = {
      mun * 2
    }

    //    rdd.map(mapFunction).collect().foreach(println)

    // 简单的逻辑直接写匿名函数
    rdd.map(_ * 2).collect().foreach(println)
    sparkContext.stop()
  }

}
