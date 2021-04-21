package com.test.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSQL_UDF {

  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL_UDF")
    //    val context = new SparkContext(sparkConf)
    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()


    // dataFrame
    val df: DataFrame = sparkSession.read.json("datas/user.json")

    df.createOrReplaceTempView("user")

    // 自定义udf函数，放到spark中
    sparkSession.udf.register("addName", (name: String) => {
      "Name:" + name
    })


    sparkSession.sql("select age,addName(username) from user").show


    sparkSession.stop()
  }

}
