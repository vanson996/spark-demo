package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 统计各省份广告点击的前三名
 */
object Spark_RDD_Req {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Opertor")
    val sparkContext = new SparkContext(sparkConf)

    // 案例实操  原始数据： 时间戳，省份，城市，用户，广告
    val dataRDD = sparkContext.textFile("datas/agent.log")

    val datas: RDD[((String, String), Int)] = dataRDD.map(s => {
      val strings = s.split(" ")
      ((strings(1), strings(4)), 1)

    })
    // 按照省份和广告聚合分组
    val reduce: RDD[((String, String), Int)] = datas.reduceByKey(_ + _)

    // 将结构转换
    val value = reduce.map {
      case ((prov, ad), sum) => (prov, (ad, sum))
    }

    // 将数据按照省份分组
    val groupRDD: RDD[(String, Iterable[(String, Int)])] = value.groupByKey()

    // 组内排序
    val sortRDD = groupRDD.mapValues(iter => {
      iter.toList.sortBy(_._2)(Ordering.Int.reverse).take(3)
    })

    sortRDD.collect().foreach(println)

    sparkContext.stop()
  }

}
