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
    testPut();

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

     for(x <- s to sm-i by i) {
      println("---------------Linked List---------------")
      rnd = new Random(42);
      writeToNetwork()
      printWriteCount(network,serverList.size)
      printPutCount(network)
      resetAllCounters(network)
      rnd = new Random(42);
      println("---------------Fingering---------------")
      writeToNetworkWithFinger()
      printWriteCount(network,serverList.size)
      printPutFingerCount(network)
      resetAllCounters(network)
       
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
    }
  }

  def writeToNetwork() : Unit = {
    for (x <- 0 to w) {
      var i = rnd.nextInt(s+1)
      serverList(i).put(rnd.nextInt(e+1), "lol"+x)
    }
  }
  def writeToNetworkWithFinger() : Unit = {
    for (x <- 0 to w) {
      var i = rnd.nextInt(s+1)
      var key = rnd.nextInt(e+1);
      serverList(i).putWithFinger(key, "lol"+x)
    }
  }
  def printWriteCount(network : Server,size : Int): Unit = {
    foreach(network, s => println(s.getServerId() + " " +
                                  //s.writeCount + " " +
                                  s.messageCount));
    println
  }

  def printPutCount(network : Server): Unit = {
    var total = 0
    foreach(network, total += _.putCount);
    println("Sum " + total)
  }
  def resetAllCounters(network : Server): Unit = {
    foreach(network, _.resetCounters);
  }
  def printPutFingerCount(network : Server): Unit = {
    var total = 0
    foreach(network, total += _.putWithFingerCount);
    println("Sum " + total)
  }
  
  
  def foreach(network:Server, callback: (Server) => Unit){
    var cur = network
    while (cur.nextServer != network) {
      callback(cur);
      cur = cur.nextServer;
    }
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
