package com.test.bigdata.spark.core.test

import java.io.ObjectOutputStream
import java.net.Socket

object Driver {
  def main(args: Array[String]): Unit = {
    // 链接服务器
    val client = new Socket("localhost", 9999)
    val stream = client.getOutputStream
    val objectOutputStream = new ObjectOutputStream(stream)
    //    stream.write(2)
    val task = new Task()
    objectOutputStream.writeObject(task)
    objectOutputStream.flush()
    objectOutputStream.close()
    client.close()
  }

}
