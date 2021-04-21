package com.test.bigdata.spark.core.req

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 热门商品前10 排序
 */
object Spark_Req1_HotCategoryTop10Analysis {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("HotCategoryTop10Analysis")
    val context = new SparkContext(sparkConf)

    // 1.读取原始日志数据
    val datasRDD = context.textFile("datas/user_visit_action.txt")

    // datasRDD重复使用，放在缓存中用以提升性能
    datasRDD.cache()

    // 2.统计品类的点击数量：（品类id，点击数量）
    val orderActionRDD: RDD[String] = datasRDD.filter(data => {
      val strings = data.split("_")
      strings(6) != "-1"
    })
    // 统计商品的点击数
    val orderCountRDD: RDD[(String, Int)] = orderActionRDD.map(action => {
      val strings = action.split("_")
      (strings(6), 1)
    }).reduceByKey(_ + _)

    // 3.统计品类的下单数量（品类id，下单数量）
    val clickActionRDD: RDD[String] = datasRDD.filter(data => {
      val strings = data.split("_")
      strings(8) != "null"
    })
    // 统计商品的下单数量
    val clickCountRDD: RDD[(String, Int)] = clickActionRDD.flatMap(action => {
      val strings = action.split("_")
      val products = strings(8).split(",")
      products.map((_, 1))
    }).reduceByKey(_ + _)

    // 4.统计品类的支付数量（品类id，支付数量）

    val payActionRDD = datasRDD.filter(action => {
      val strings = action.split("_")
      strings(10) != "null"
    })
    val payCountRDD = payActionRDD.flatMap(action => {
      val strings = action.split("_")
      strings(10).split(",").map((_, 1))
    }).reduceByKey(_ + _)

    // 5.将品类进行排序，取前十名

    // 使用 cogroup  可能会进行 shuffle 操作印象性能
    /*val cogroupRDD: RDD[(String, (Iterable[Int], Iterable[Int], Iterable[Int]))] =
      clickCountRDD.cogroup(orderCountRDD, payCountRDD)

    val analysisRDD: RDD[(String, (Int, Int, Int))] = cogroupRDD.mapValues {
      case (clickIter, orderIter, payIter) => {
        var clickCount = 0
        val iter1 = clickIter.iterator
        if (iter1.hasNext) {
          clickCount = iter1.next()
        }
        var orderCount = 0
        val iter2 = orderIter.iterator
        if (iter2.hasNext) {
          orderCount = iter2.next()
        }
        var payCount = 0
        val iter3 = payIter.iterator
        if (iter1.hasNext) {
          payCount = iter3.next()
        }
        (clickCount, orderCount, payCount)
      }
    }*/

    val click = clickCountRDD.map {
      case (cid, count) => {
        (cid, (count, 0, 0))
      }
    }
    val order = orderCountRDD.map {
      case (cid, count) => {
        (cid, (0, count, 0))
      }
    }
    val pay = payCountRDD.map {
      case (cid, count) => {
        (cid, (0, 0, count))
      }
    }
    // 将三个数据源合并在在一起，统一进行聚合计算
    val sourceRDD = click.union(order).union(pay)
    val analysisRDD = sourceRDD.reduceByKey((v1, v2) => {
      (v1._1 + v2._1, v1._2 + v2._2, v1._3 + v2._3)
    })

    // 点击数量排序，下单数量排序，支付数量排序
    val resultRDD = analysisRDD.sortBy(_._2, false).take(10)

    resultRDD.foreach(println)

    context.stop()


  }

}
