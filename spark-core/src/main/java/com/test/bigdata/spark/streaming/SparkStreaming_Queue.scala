package com.test.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

object SparkStreaming_Queue {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming_Queue")
    // 第二个参数 org.apache.spark.streaming.Duration 表示批量处理的周期（采集周期）
    val context = new StreamingContext(conf, Seconds(3))

    val rddQueue = new mutable.Queue[RDD[Int]]()

    val inputStream = context.queueStream(rddQueue)

    // 处理队列中的 RDD 数据
    val mappedStream = inputStream.map((_,1))
    val reducedStream = mappedStream.reduceByKey(_ + _)
    // 打印结果
    reducedStream.print()

    // 1.启动采集器
    context.start()

    // 循环创建并向 RDD 队列中放入 RDD
    for (i <- 1 to 5) {
      rddQueue += context.sparkContext.makeRDD(1 to 300, 10)
      Thread.sleep(2000)
    }

    // 2.等待采集器的关闭
    context.awaitTermination()
    //    context.stop()
  }

}
