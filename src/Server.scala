/**
  * Created by centos on 1/26/17.
  */
  
// Id = hash id for this server.
// n = how many clones 
class Server(id: Int, n: Int, circleSize:Int) {
  private var writeCount : Int;
  private var cloneServerIds : Array[Int] = new Array[Int](n);
  private var data : collection.mutable.Map[Int, String];

  var nextServer : Server;
  var prevServer : Server;
  
  def get(key: Int) : String { }
  def put(key: Int, data : String) { }

  def findSuccessor(key : Int):Server {
    if (id > nextServer.id){ 
      // the bridge.
      if (key > id || key < nextServer.id ) return nextServer;
    }
    if (id < key && nextServer.id > key) return nextServer;

    return nextServer.findSuccessor(key);
  }

  def connect(network : Server): Unit = { 
    // Here we need to split the data space between this Server and the existingServer.
    nextServer = network.findSuccessor(id);
    prevServer = nextServer.prevServer;

    prevServer.nextServer = this;
    nextServer.prevServer = this;
  }
}
