package com.test.bigdata.spark.core.rdd.builder

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_RDD_File1 {

  def main(args: Array[String]): Unit = {
    // todo 准备环境
    // [*]表示本机最大核数
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sparkContext = new SparkContext(sparkConf)
    // todo 创建RDD
    // textFile : 是以行为单位读取数据
    // wholeTextFiles：是以文件为单位读取数据，读取的结果为元组，第一个元素表示文件路径，第二个元素表示文件内容
    val files = sparkContext.wholeTextFiles("datas")
    files.collect().foreach(println)
    // todo 关闭环境
    sparkContext.stop()
  }

}
