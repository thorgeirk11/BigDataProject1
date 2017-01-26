/**
  * Created by centos on 1/26/17.
  */

object Main {

  def main(args: Array[String]): Unit = {
    var s = 10;
    var e = 10000;
    var n = 3;
    var w = 1000000;
    var i = 5;
    var sm = 30;
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
