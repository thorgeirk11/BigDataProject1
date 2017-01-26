/**
  * Created by centos on 1/26/17.
  */

import scala.util

object Main {
  var s = 10;
  var e = 10000;
  var n = 3;
  var w = 1000000;
  var i = 5;
  var sm = 30;

  def main(args: Array[String]): Unit = {
    parsePara(args);

    var rnd = new Random();
    var network = new Server(rnd.nextInt(e+1),n);
    for (var x <- 0..s)
      network.connect(new Server(rnd.nextInt(e+1),n));

    for (var x <- 0..w)
      network.put(rnd.nextInt(e+1),"lol"+x);
  }

  def parsePara(args: Array[String]):Unit{
    for (arg <- args) {
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
