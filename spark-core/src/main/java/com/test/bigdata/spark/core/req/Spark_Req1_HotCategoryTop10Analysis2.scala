package com.test.bigdata.spark.core.req

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 热门商品前10 排序
 */
object Spark_Req1_HotCategoryTop10Analysis2 {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10Analysis2")
    val context = new SparkContext(sparkConf)

    // 1.读取原始日志数据
    val datasRDD = context.textFile("datas/user_visit_action.txt")

    // 将数据转换结构
    val flatRDD = datasRDD.flatMap(str => {
      val strings = str.split("_")
      if (strings(6) != "-1") {
        // 点击操作
        List((strings(6), (1, 0, 0)))
      } else if (strings(8) != "null") {
        // 下单场合
        val orders = strings(8).split(",")
        orders.map((_, (0, 1, 0)))
      } else if (strings(10) != "null") {
        val pays = strings(10).split(",")
        pays.map((_, (0, 0, 1)))
      } else {
        Nil
      }
    })

    val analysisRDD = flatRDD.reduceByKey((v1, v2) => {
      (v1._1 + v2._1, v1._2 + v2._2, v1._3 + v2._3)
    })

    // 点击数量排序，下单数量排序，支付数量排序
    val resultRDD = analysisRDD.sortBy(_._2, false).take(10)

    resultRDD.foreach(println)

    context.stop()


  }

}
