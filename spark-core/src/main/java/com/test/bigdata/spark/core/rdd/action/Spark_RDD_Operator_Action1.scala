package com.test.bigdata.spark.core.rdd.action

import org.apache.spark.{SparkConf, SparkContext}

object Spark_RDD_Operator_Action1 {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("Operator")
    val context = new SparkContext(conf)

    // todo -- 行动算子
    // 行动算子，就是触发作业执行的方法




    context.stop()

  }
}
