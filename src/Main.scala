/**
  * Created by centos on 1/26/17.
  */

import scala.util
import scala.util.Random

object Main {
  var s = 100;
  var e = 10000;
  var n = 3;
  var w = 1000000;
  var i = 5;
  var sm = 30;

  def main(args: Array[String]): Unit = {
    
/**
    
    //var network = buildTestNetwork(30, 2, 64).nextServer;
    var network = new Server(15, 1, 30);
    network.buildFingerTable();
    network.connect(new Server(10, 1, 30));
    //network.connect(new Server(20, 1, 30));
    //network.connect(new Server(25, 1, 30));
    network.connect(new Server(5, 1, 30));

    network.put(0,"asd");
    network.put(0,"asd");
    network.put(0,"asd");
    network.put(30,"asd");
    network.put(30,"asd");
    print(network)
    println(network.getWithFinger(30));
    println(network.getWithFinger(0));
    //println(60)
    //network.put(60, "_")
    //println(30)
    //network.put(0, "_")
    //println(20)
    //network.put(20, "_")
*/

/**
    print(network)
    println();
    println("NewNode 15")
    var n15 = new Server(15, 2, 64);
    network.connect(n15);
    //for (x <- n15.fingerTable) println(x.getServerId());
    print(network)
    
*/
    test1();
  }


  def buildTestNetwork(size: Int, replicationFactor: Int, c:Int): Server = {
    var network = new Server(2, replicationFactor,c);
    var x = 0;
    for (x <- 2 to size) {
      network.connect(new Server(x * 2, replicationFactor,c));
    }
    return network;
  }

  def print(network: Server): Unit = {
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
  var intList:List[Int] = List()
  var rnd = new Random(42);

  def test1(): Unit = {
    var network = new Server(rnd.nextInt(e + 1), n,e);
    network.buildFingerTable();
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
    //printWriteCount(network,s)
    //begin test 2
    /**
     for(x <- s to sm by i) {
     
      var j = 0
      for (j <- 0 to i) {
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
      //printWriteCount(network,x)
    }
    */
    print(network);
    
    println

    //for (x <- 1 to 1000) {
      var i = rnd.nextInt(s+1)
      var key = rnd.nextInt(e+1);
      println("Entry " + serverList(i).getServerId() + " key " + key);
      serverList(i).getWithFinger(key)
    //}
        
    println("NetworkSize: " + sm)
    var cur = network
    while (cur.nextServer != network) {
      println(cur.getServerId() + ": messageCount " + cur.messageCount);
      cur = cur.nextServer;
    }
    println("-----------------------")
  }
  def writeToNetwork() : Unit = {
    var x = 0
    for (x <- 0 to w) {
      var i = rnd.nextInt(s+1)
      serverList(i).put(rnd.nextInt(e+1), "lol"+x)
    }
  }
  def printWriteCount(network : Server,size : Int): Unit = {
    println("NetworkSize: " + size)
    var cur = network
    for (x <- 0 to size) {
      println(cur.getServerId() + ": " + cur.writeCount);
      cur = cur.nextServer;
    }
    println("-----------------------")

  }
  def parsePara(args: Array[String]):Unit = {
    for (arg <- args) {
      println(arg);
      if (arg == "-s"){
        s = args.tail.head.toInt;
      }
      if (arg == "-e"){
        e = args.tail.head.toInt;
      }
      if (arg == "-n"){
        n = args.tail.head.toInt;
      }
      if (arg == "-w"){
        w = args.tail.head.toInt;
      }
      if (arg == "-i"){
        i = args.tail.head.toInt;
      }
      if (arg == "-sm"){
        sm = args.tail.head.toInt;
      }
    }
  }

}
