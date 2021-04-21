package com.test.bigdata.spark.core.test

import java.io.ObjectInputStream
import java.net.ServerSocket

object Executor {
  def main(args: Array[String]): Unit = {

    // 启动服务器接收数据
    val server = new ServerSocket(9999)

    println("服务器启动。。。")
    // 等待客户端的链接

    val client = server.accept()
    val inputStream = client.getInputStream
    val objectInputStream = new ObjectInputStream(inputStream)
    //    val i = inputStream.read()
    val task = objectInputStream.readObject().asInstanceOf[Task]
    val list = task.compute()
    println("计算的结果为：" + list)
    inputStream.close()
    client.close()
    server.close()

  }

}
