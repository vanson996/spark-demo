package com.test.bigdata.spark.core.rdd.builder

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark_RDD_Memory {

  def main(args: Array[String]): Unit = {
    // todo 准备环境
    // [*]表示本机最大核数
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    val sparkContext = new SparkContext(sparkConf)
    // todo 创建RDD
    // 从内存中创建RDD，将内存中集合的数据作为处理的数据源
    val seq = Seq[Int](1, 2, 3)
    // parallelize：并行
    //    val rdd: RDD[Int] = sparkContext.parallelize(seq)
    // makeRDD 底层调用的是 parallelize 方法
    val rdd: RDD[Int] = sparkContext.makeRDD(seq)
    rdd.collect().foreach(println)


    // todo 关闭环境
    sparkContext.stop()
  }

}
