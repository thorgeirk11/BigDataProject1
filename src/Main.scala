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
    //parsePara(args)
    var rnd = new Random(72);
    var network = new Server(rnd.nextInt(e+1),n);
    var x = 0;
    for (x <- 0 to s) {
      var k = rnd.nextInt(e+1);
      network.connect(new Server(k, n));
      println(k);
    }
    for (x <- 0 to w)
      network.put(rnd.nextInt(e+1),"lol"+x);
    for (x <- 0 to s) {
      println(network.getServerId() + ": " + network.writeCount);
      network = network.nextServer;
    }
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
