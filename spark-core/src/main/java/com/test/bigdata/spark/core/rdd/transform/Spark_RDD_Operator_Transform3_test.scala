package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * 对不同类型的数据进行扁平映射处理
 */
object Spark_RDD_Operator_Transform3_test {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Opertor")
    val sparkContext = new SparkContext(sparkConf)

    // 算子- flatmap
    val rdd = sparkContext.makeRDD(List(List(1, 2), 4, List(5, 6)))
    // 使用模式匹配 将不同类型的数据进行处理，之后再进行扁平处理
    val value= rdd.flatMap(data => {
      data match {
        case list: List[_] => list
        case  dat => List(dat)
      }
    })
    value.collect().foreach(println)

    sparkContext.stop()
  }

}
