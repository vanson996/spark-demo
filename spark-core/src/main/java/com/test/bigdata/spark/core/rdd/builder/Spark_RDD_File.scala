package com.test.bigdata.spark.core.rdd.builder

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_RDD_File {

  def main(args: Array[String]): Unit = {
    // todo 准备环境
    // [*]表示本机最大核数
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sparkContext = new SparkContext(sparkConf)
    // todo 创建RDD
    // 从文件中创建RDD，将文件中的数据作为处理的数据源
    // path 文件路径默认是以当前环境的根路径作为基准。可以写绝对路径也可以写相对路径
    // path 可以指定目录名称，也可以使用通配符
    val files: RDD[String] = sparkContext.textFile("datas/1.txt")
      files.collect().foreach(println)
    // todo 关闭环境
    sparkContext.stop()
  }

}
