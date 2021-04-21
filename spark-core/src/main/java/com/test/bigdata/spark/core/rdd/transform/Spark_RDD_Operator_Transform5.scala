package com.test.bigdata.spark.core.rdd.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}


object Spark_RDD_Operator_Transform5 {
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("Opertor")
    val sparkContext = new SparkContext(sparkConf)

    // todo 算子- groupBy
    // 将同一个分区的数据直接转换为相同类型的内存数组进行处理，分区不变
    val rdd = sparkContext.makeRDD(List(1, 2, 3, 4), 2)


    // groupBy 会将数据源中的每个数据进行分组判断，根据返回的分组key进行分组
    // 相同的key值的数据会防止在一个组中
    def groupFunction(num: Int): Int = {
      num % 2
    }


    val value = rdd.groupBy(groupFunction)
    value.collect().foreach(println)

    sparkContext.stop()
  }

}
