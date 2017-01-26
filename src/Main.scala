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

  def main(args: Array[String]): Unit = {

    var n1 = new Server(10,2);
    var n2 = new Server(20,2);
    var n3 = new Server(30,2);
    var n4 = new Server(40,2);
    var n5 = new Server(50,2);
    var n6 = new Server(60,2);

    var network = n1.connect(n2).connect(n3).
                     connect(n4).connect(n5).
                     connect(n6);

    network.put(15,"20/30");
    network.put(25,"30/40");
    network.put(26,"30/40");

    println("Node 10:")
    n1.dataMap.foreach(println);
    println("Node 20:")
    n2.dataMap.foreach(println);
    println("Node 30:")
    n3.dataMap.foreach(println);
    println("Node 40:")
    n4.dataMap.foreach(println);
    println("Node 50:")
    n5.dataMap.foreach(println);


    /**
    var rnd = new Random(42);
    var network = new Server(rnd.nextInt(e+1),n);
    var x = 0;
    for (x <- 0 to s) {
      var k = rnd.nextInt(e+1);
      while (k == network.findSuccessor(k).getServerId()) {
        k = rnd.nextInt(e+1);
      }
      network.connect(new Server(k, n));
    }
    
    var cur = network.nextServer;
    while(cur != network){
       println(cur.getServerId());
       cur = cur.nextServer;
    }
*/

    /**
    for (x <- 0 to w)
      network.put(rnd.nextInt(e+1),"lol"+x);
    for (x <- 0 to s) {
      println(network.getServerId() + ": " + network.writeCount);
      network = network.nextServer;
    }
    
    var k = 0;
    while (k == network.findSuccessor(k).getServerId()) {
      k = rnd.nextInt(e+1);
    }
    new Server(k, n).connect(network);
    **/
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
