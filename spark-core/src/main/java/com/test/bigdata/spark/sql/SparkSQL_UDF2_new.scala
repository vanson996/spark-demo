package com.test.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession, functions}

object SparkSQL_UDF2_new {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL_UDF")
    //    val context = new SparkContext(sparkConf)
    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()


    // dataFrame
    val df: DataFrame = sparkSession.read.json("datas/user.json")

    df.createOrReplaceTempView("user")

    // 自定义udf函数，放到spark中
    sparkSession.udf.register("ageAvg", functions.udaf(new MyAvgUDAF()))

    sparkSession.sql("select ageAvg(age) from user").show


    sparkSession.stop()
  }

  /**
   * 自定义聚合函数类：计算年龄的平均值
   * 1.继承org.apache.spark.sql.expressions.Aggregator，定义泛型
   * IN:  输入的数据类型
   * BUF: 缓冲区数据类型
   * OUT: 输出的数据类型
   * 2.重写方法
   */

  case class Buff(var totalAge: Long, var count: Long)

  class MyAvgUDAF extends Aggregator[Long, Buff, Long] {
    // 初始值，缓冲区的初始化
    override def zero: Buff = {
      Buff(0, 0)
    }

    // 根据输入的数据更新缓冲区的数据
    override def reduce(b: Buff, a: Long): Buff = {
      b.totalAge = b.totalAge + a
      b.count = b.count + 1
      b
    }

    // 合并缓冲区
    override def merge(b1: Buff, b2: Buff): Buff = {
      b1.totalAge = b1.totalAge + b2.totalAge
      b1.count = b1.count + b2.count
      b1
    }

    // 计算结果
    override def finish(reduction: Buff): Long = {
      reduction.totalAge / reduction.count
    }

    // 缓冲区编码操作
    override def bufferEncoder: Encoder[Buff] = Encoders.product

    // 输出的编码操作
    override def outputEncoder: Encoder[Long] = Encoders.scalaLong
  }

}
