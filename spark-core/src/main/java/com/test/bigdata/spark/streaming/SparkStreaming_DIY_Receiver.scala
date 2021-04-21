package com.test.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}

import java.util.Random


object SparkStreaming_DIY_Receiver {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming_DIY_Receiver")
    val context = new StreamingContext(conf, Seconds(3))

    val messageStream = context.receiverStream(new MyReceiver)

    messageStream.print()

    context.start()
    context.awaitTermination()

  }

  /**
   * 自定义数据收集器
   * 1.继承抽象的 Receiver，定义泛型
   */
  class MyReceiver extends Receiver[String](StorageLevel.MEMORY_ONLY) {

    private var flag = true

    override def onStart(): Unit = {
      new Thread(new Runnable {
        override def run(): Unit = {
          while (flag) {
            val message = "采集的数据为：" + new Random().nextInt(100).toString
            store(message)
            Thread.sleep(500)
          }
        }
      }).start()
    }


    override def onStop(): Unit = {
      flag = false
    }
  }

}
