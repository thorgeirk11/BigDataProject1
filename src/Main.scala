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
  var sm = 35;
  var rnd = new Random();
  def main(args: Array[String]): Unit = {
    parsePara(args)
    println("Parameters: S: " + s + " , E: " + e + " , N: "+ n + " , W: "+ w + " , I:" + i + " , Sm: " + sm)
    println("Resetting counter between each W random write operations")
    testPut();

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
    for (x <- 1 to s-1) {
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
       
      println
      println("network size: " + (serverList.size));
      rnd = new Random(42);
      writeToNetwork()
      rnd = new Random(42);
      writeToNetworkWithFinger()
      printCounts(network,serverList.size)
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
    for (x <- 0 to w-1) {
      var i = rnd.nextInt(s)
      serverList(i).put(rnd.nextInt(e+1), "lol"+x)
    }
  }
  def writeToNetworkWithFinger() : Unit = {
    for (x <- 0 to w-1) {
      var i = rnd.nextInt(s)
      serverList(i).putWithFinger(rnd.nextInt(e), "lol"+x)
    }
  }
  def printCounts(network : Server,size : Int): Unit = {
    foreach(network, s => println("id: " + s.getServerId() + 
                                  " putCount: " + s.putCount + 
                                  " putWithFingerCount: " +s.putWithFingerCount));

    //foreach(network, s => {
    //  var prev = s.prevServer.getServerId();
    //  var sid = s.getServerId();
    //  if (prev > s.getServerId()) sid += e;
    //    println(s.getServerId() + " " + (sid-prev) + " " +  (sid-prev) / (e*1.0)   + " " + s.writeCount)
    //});
    println
  }

  def printPutCount(network : Server): Unit = {
    var total = 0
    foreach(network, total += _.putCount);
    println("Sum " + total)
  }
  def resetAllCounters(network : Server): Unit = {
    foreach(network, _.resetCounters());
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
    callback(cur);
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
