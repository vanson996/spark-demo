package com.test.bigdata.spark.streaming

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreaming_State {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming_State")
    // 第二个参数 org.apache.spark.streaming.Duration 表示批量处理的周期（采集周期）
    val context = new StreamingContext(conf, Seconds(3))
    // 使用有状态操作时，需要设定检查点路径
    context.checkpoint("cp")
    // 获取端口数据
    val lines = context.socketTextStream("localhost", 9999)
    val words = lines.map((_, 1))


    // updateStateByKey 根据 key 对数据的状态进行更新
    // seq 表示相同的 key 的 value 值
    // opt 表示缓冲区中相同 key 的 value 值
    val state: DStream[(String, Int)] = words.updateStateByKey {
      case (seq: Seq[Int], opt: Option[Int]) => {
        val count = opt.getOrElse(0) + seq.sum
        Option(count)
      }
    }

    state.print()
    // 1.启动采集器
    context.start()
    // 2.等待采集器的关闭
    context.awaitTermination()
    //    context.stop()
  }

}
