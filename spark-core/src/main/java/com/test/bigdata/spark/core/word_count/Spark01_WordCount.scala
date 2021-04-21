package com.test.bigdata.spark.core.word_count

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Spark01_WordCount {

  def main(args: Array[String]): Unit = {


    // 建立和spark框架的连接
    val conf = new SparkConf().setMaster("local").setAppName("WordCount")
    val context = new SparkContext(conf)
    // 执行业务操作
    // 1.读取文件获取单行数据
    val lines: RDD[String] = context.textFile("datas")
    // 2.将单行数据分词
    val words: RDD[String] = lines.flatMap(_.split(" "))
    // 3.将数据单词进行分组，便于统计
    //    val wordGroup: RDD[(String, Iterable[String])] = words.groupBy(word => word)
    // 4.对分组后的数据进行转换
    //    val value = wordGroup.map {
    //      case (word, list) => (word, list.size)
    //    }
    //    val array: Array[(String, Int)] = value.collect()
    //    array.foreach(println)

    // Spark框架提供了更多功能，可以将分组聚合使用一个方法实现
    val wordToOne = words.map(word => (word, 1))
    // reduceByKey: 相同的key的数据，可以对value进行reduce聚合
    val result = wordToOne.reduceByKey(_ + _)
    val array = result.collect()
    array.foreach(println)

    // 关闭连接
    context.stop()
  }

}
