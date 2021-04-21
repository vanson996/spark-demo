package com.test.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql._

import java.util.Properties

/**
 * 从数据库中读取数据
 */
object SparkSQL_JDBC_Connect {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkSQL_JDBC_Connect")
    val sparkSession = SparkSession.builder().config(conf).getOrCreate()

    // 方式一：使用通用的load方法读取
    /*sparkSession.read.format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/db_test").option("driver", "com.mysql.jdbc.Driver")
      .option("user","root").option("password","root").option("dbtable","user")
      .load().show()*/


    // 方式2：使用通用的 load 方法读取

    /*sparkSession.read.format("jdbc")
      .options(Map("url" -> "jdbc:mysql://localhost:3306/db_test?user=root&password=root",
        "dbtable" -> "user",
        "driver" -> "com.mysql.jdbc.Driver")).load().show()
*/

    // 方式3：使用 jdbc 方法读取
    val properties = new Properties()
    properties.setProperty("user", "root")
    properties.setProperty("password", "root")
    val dataFrame = sparkSession.read.jdbc("jdbc:mysql://localhost:3306/db_test", "user", properties)
    // 将 dataFrame 转换为 DataSet
    val value: Dataset[User] = dataFrame.as[User](Encoders.product)
    value.foreach(user => println(user.name))
    value.show()
  }


  case class User(id: Int, name: String, age: Int)

}

