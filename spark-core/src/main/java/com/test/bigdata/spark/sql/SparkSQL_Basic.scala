package com.test.bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.SparkConf

object SparkSQL_Basic {

  def main(args: Array[String]): Unit = {
    //todo 创建spark SQL的运行环境
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    //    val context = new SparkContext(sparkConf)
    val sparkSession = SparkSession.builder().config(sparkConf).getOrCreate()


    // todo 执行逻辑操作
    // dataFrame
    val df: DataFrame = sparkSession.read.json("datas/user.json")
    //    df.show()

    // dataFrame => SQL
    /*df.createOrReplaceTempView("user")
    sparkSession.sql("select * from user").show()
    sparkSession.sql("select username,age from user").show()
    sparkSession.sql("select max(age) from user").show()*/

    // dataFrame => DSL
    // 在使用DataFrame时，如果涉及到转换操作，需要引入转换规则
    import sparkSession.implicits._
    /*df.select("username","age").show()
    df.select($"age"+1).show()*/

    // todo:DataSet
    // dataFrame是特定泛型的DataSet
    val seq = Seq(1, 2, 3, 4)
    val ds = seq.toDS()
    ds.show()





    sparkSession.stop()
  }

}
