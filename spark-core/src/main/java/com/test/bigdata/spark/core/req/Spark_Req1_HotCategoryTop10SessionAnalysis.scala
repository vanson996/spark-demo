package com.test.bigdata.spark.core.req

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 热门商品前10 中 每个商品的 top10 活跃 session统计
 */
object Spark_Req1_HotCategoryTop10SessionAnalysis {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10Analysis2")
    val context = new SparkContext(sparkConf)

    // 1.读取原始日志数据
    val datasRDD = context.textFile("datas/user_visit_action.txt")


    val top10 = top10Category(datasRDD)

    // 1.过滤原始数据，保留点击的前10品类ID
    val click = datasRDD.filter(action => {
      val strings = action.split("_")
      if (strings(6) != "-1") {
        top10.contains(strings(6))
      } else {
        false
      }
    })

    // 根据品类ID和sessionId进行点击量的统计 （(productId,sessionId),sum）
    val reduceRDD = click.map(action => {
      val strings = action.split("_")

      ((strings(6), strings(2)), 1)
    }).reduceByKey(_ + _)

    val mapRDD = reduceRDD.map {
      case ((pid, sid), sum) => (pid, (sid, sum))
    }

    val groupRDD = mapRDD.groupByKey()
    val resultRDD = groupRDD.mapValues(iter => {
      iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(10)
    })


    resultRDD.collect().foreach(println)
    context.stop()


  }

  def top10Category(actionRDD: RDD[String]) = {
    // 将数据转换结构
    val flatRDD = actionRDD.flatMap(str => {
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
    analysisRDD.sortBy(_._2, false).take(10).map(_._1)


  }

}
