package com.test.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming_WordCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming_WordCount")
    // 第二个参数 org.apache.spark.streaming.Duration 表示批量处理的周期（采集周期）
    val context = new StreamingContext(conf, Seconds(3))

    // 获取端口数据
    val lines = context.socketTextStream("localhost", 9999)
    val words = lines.flatMap(_.split(" ")).map((_, 1))

    val value: DStream[(String, Int)] = words.reduceByKey(_ + _)

    value.print()

    // 由于 SparkStreaming 采集器是长期执行的任务，所以不能直接关闭
    // 如果 main 方法执行完毕，应用程序也会自动结束。所以不能让 main 方法执行完毕

    // 1.启动采集器
    context.start()
    // 2.等待采集器的关闭
    context.awaitTermination()
    //    context.stop()
  }

}
