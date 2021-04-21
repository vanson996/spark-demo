package com.test.bigdata.spark.core.req

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 热门商品前10 中 每个商品的 top10 活跃 session统计
 */
object Spark_Req1_PageFlowAnalysis {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Spark_Req1_PageFlowAnalysis")
    val context = new SparkContext(sparkConf)

    // 1.读取原始日志数据
    val datasRDD = context.textFile("datas/user_visit_action.txt")

    val objRDD: RDD[UserVisitAction] = datasRDD.map(action => {
      val strings = action.split("_")
      UserVisitAction(
        strings(0),
        strings(1).toLong,
        strings(2),
        strings(3).toLong,
        strings(4),
        strings(5),
        strings(6).toLong,
        strings(7).toLong,
        strings(8),
        strings(9),
        strings(10),
        strings(11),
        strings(12).toLong
      )
    })

    objRDD.cache()

    // 计算分母
    val total = objRDD.map(action => (action.page_id, 1)).reduceByKey(_ + _).collect().toMap

    // 计算分子
    // 根据session会话进行分组
    val sessionRDD: RDD[(String, Iterable[UserVisitAction])] = objRDD.groupBy(_.session_id)

    //[1,2,3,4] ==> [(1,2),(2,3),(3,4)]
    val mapRDD = sessionRDD.mapValues(iter => {
      val actions = iter.toList.sortBy(_.action_time)
      val flowIds = actions.map(_.page_id)
      val pageFlowIds: List[(Long, Long)] = flowIds.zip(flowIds.tail)
      pageFlowIds.map(tuples => (tuples, 1))
    })

    val flatRDD = mapRDD.map(_._2).flatMap(list => list)
    val dataRDD = flatRDD.reduceByKey(_ + _)
    dataRDD.foreach {
      case ((page1, page2), sum) => {
        val totalCount = total.getOrElse(page1, 0)
        println(s"页面 ${page1} 跳转到 ${page2} 的单跳转换率为： " + (sum.toDouble / totalCount))
      }
    }

    context.stop()
  }

  //用户访问动作表
  case class UserVisitAction(
                              date: String, //用户点击行为的日期
                              user_id: Long, //用户的 ID
                              session_id: String, //Session 的 ID
                              page_id: Long, //某个页面的 ID
                              action_time: String, //动作的时间点
                              search_keyword: String, //用户搜索的关键词
                              click_category_id: Long, //某一个商品品类的 ID
                              click_product_id: Long, //某一个商品的 ID
                              order_category_ids: String, //一次订单中所有品类的 ID 集合
                              order_product_ids: String, //一次订单中所有商品的 ID 集合
                              pay_category_ids: String, //一次支付中所有品类的 ID 集合
                              pay_product_ids: String, //一次支付中所有商品的 ID 集合
                              city_id: Long
                            ) //城市 id
}
