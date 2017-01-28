/**
  * Created by centos on 1/26/17.
  */

import scala.util
import scala.util.Random

object Main {
  var s = 10;
  var e = 10000;
  var n = 3;
  var w = 1000000;
  var i = 5;
  var sm = 30;
  var rnd = new Random();
  def main(args: Array[String]): Unit = {
    parsePara(args)
    println("Parameters: S: " + s + " , E: " + e + " , N: "+ n + " , W: "+ w + " , I:" + i + " , Sm: " + sm)
    println("Resetting counter between each W random write operations")
    println("---------------------")
    println("Testing writes by just going in a circle")
    testPut();

    println("---------------------")
    println("Testing writes by finger table")
    testPutFinger()

  }

  def printFingerTable(network: Server): Unit = {
    var cur = network
    while (cur.nextServer != network) {
      println("Node " + cur.getServerId() + ":")
      for (x <- cur.fingerTable) println(x.getServerId());
      //cur.dataMap.foreach(println);
      cur = cur.nextServer;
    }
    println("Node " + cur.getServerId() + ":")
    for (x <- cur.fingerTable) println(x.getServerId());
    //cur.dataMap.foreach(println);
  }
  var serverList:List[Server] = List()
  var serverList2:List[Server] = List()
  var intList:List[Int] = List()
  var intList2:List[Int] = List()


  def testPutFinger() : Unit = {
    var network = new Server(rnd.nextInt(e + 1), n,e);
    network.buildFingerTable();

    serverList2 ::= network
    intList2 ::= network.getServerId()
    var x = 0;
    for (x <- 1 to s) {
      var k = rnd.nextInt(e + 1);
      while (intList2.contains(k)) {
        k = rnd.nextInt(e + 1);
      }
      var tmp = new Server(k,n,e)
      network.connect(tmp);
      serverList2 ::= tmp
      intList2 ::= k
    }

    writeToNetworkWithFinger()
    printWriteCount(network, serverList2.size)
    printPutFingerCount(network)
    resetAllCounters(network)
    //begin test 2

    for(x <- s to sm-i by i) {
      println("X:" + x)
      var j = 0
      for (j <- 0 to i-1) {
        var k = rnd.nextInt(e + 1);
        while (intList2.contains(k)) {
          k = rnd.nextInt(e + 1);
        }
        var tmp = new Server(k, n, e)
        network.connect(tmp);
        serverList2 ::= tmp
        intList2 ::= k
      }
      writeToNetworkWithFinger()
      printWriteCount(network,serverList2.size)
      printPutFingerCount(network)
      resetAllCounters(network)
    }

  }
  def testPut(): Unit = {
    var network = new Server(rnd.nextInt(e + 1), n,e);
    serverList ::= network
    intList ::= network.getServerId()
    var x = 0;
    for (x <- 1 to s) {
      var k = rnd.nextInt(e + 1);
      while (intList.contains(k)) {
        k = rnd.nextInt(e + 1);
      }
      var tmp = new Server(k,n,e)
      network.connect(tmp);
      serverList ::= tmp
      intList ::= k
    }


    writeToNetwork()
    printWriteCount(network,serverList.size)
    printPutCount(network)
    resetAllCounters(network)
    //begin test 2

     for(x <- s to sm-i by i) {
      println("X:" + x)
      var j = 0
      for (j <- 0 to i-1) {
        var k = rnd.nextInt(e + 1);
        while (intList.contains(k)) {
          k = rnd.nextInt(e + 1);
        }
        var tmp = new Server(k, n, e)
        network.connect(tmp);
        serverList ::= tmp
        intList ::= k
      }
      writeToNetwork()
      printWriteCount(network,serverList.size);
      printPutCount(network)
      resetAllCounters(network)
    }

        

  }

  def writeToNetwork() : Unit = {
    var x = 0
    for (x <- 0 to w) {
      var i = rnd.nextInt(s+1)
      serverList(i).put(rnd.nextInt(e+1), "lol"+x)
    }
  }
  def writeToNetworkWithFinger() : Unit = {

    var x = 0
    for (x <- 0 to w) {
      var i = rnd.nextInt(s+1)
      var key = rnd.nextInt(e+1);
      serverList2(i).putWithFinger(key, "lol"+x)
    }
  }
  def printWriteCount(network : Server,size : Int): Unit = {
    println("NetworkSize: " + (size - 1))
    var total = 0
    var cur = network
    while(cur.nextServer!= network){
      println(cur.getServerId() + ": writeCount " + cur.writeCount);
      cur = cur.nextServer;
    }
    println("-----------------------")
  }

  def printPutCount(network : Server): Unit = {
    println("NetworkSize: " + (serverList.size-1))
    var cur = network;
    var total = 0
    while (cur.nextServer != network) {
      //println(cur.getServerId() + ": putCount " + cur.putCount);
      total += cur.putCount;
      cur = cur.nextServer;
    }
    println("-----------------------")
    println("Put alltogether: " + total)
  }
  def resetAllCounters(network : Server): Unit = {
    var cur = network
    while(cur.nextServer != network) {
      cur.resetCounters()
      cur = cur.nextServer

    }
  }
  def printPutFingerCount(network : Server): Unit = {
    println("NetworkSize: " + (serverList2.size-1))
    var cur = network
    var total = 0
    while (cur.nextServer != network) {
      //println(cur.getServerId() + ": putCount " + cur.putWithFingerCount);
      total += cur.putWithFingerCount;
      cur = cur.nextServer;
    }
    println("-----------------------")
    println("Put Finger alltogether: " + total)
  }
  def parsePara(args: Array[String]):Unit = {
    var x = 0
    for (x <- 0 to args.size -1 ) {

      if (args(x) == "-s"){
        s = args(x+1).toInt;
      }
      if (args(x) == "-e"){
        e = args(x+1).toInt;
      }
      if (args(x) =="-n"){
        n = args(x+1).toInt;
      }
      if (args(x) =="-w"){
        w = args(x+1).toInt;
      }
      if (args(x) == "-i"){
        i = args(x+1).toInt;
      }
      if (args(x) == "-sm"){
        sm = args(x+1).toInt;
      }
    }
  }

}
