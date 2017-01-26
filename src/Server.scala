/**
  * Created by centos on 1/26/17.
  */
  
// Id = hash id for this server.
// n = how many clones 
class Server(id: Int, n: Int) {
  var writeCount = 0;
  private var dataMap = collection.mutable.Map[Int, String]();

  var nextServer : Server = this;
  var prevServer : Server = this;

  def getServerId() : Int = {
    return id;
  }

  def get(key: Int) : String = {
    if (belongsToMyGroup(key)) return dataMap(key);
    else return nextServer.get(key);
  }
  def put(key: Int, data : String) : Unit = {
    if (belongsToMe(key))
    {
      var cur = this;
      var x = 0;
      for (x <- 0 to n){
        cur.writeData(key,data);
        cur = cur.nextServer;
      }
    }
    else nextServer.put(key,data);
  }
  def writeData(key:Int, data:String) :Unit = {
    writeCount+=1;
    dataMap(key) = data;
  }
  
  def findSuccessor(key : Int) : Server  = {
    if (belongsToMe(key)) return nextServer;
    else return nextServer.findSuccessor(key);
  }
  def belongsToMe(key : Int) : Boolean = {
    if (nextServer.getServerId() == id) return true;
    if (key == id) return true;
    if (this.id > nextServer.getServerId()){
      // the bridge.
      if (key > this.id || key < nextServer.getServerId() ) return true;
    }
    if (this.id < key && nextServer.getServerId() > key) return true;

    return false;
  }
  def belongsToMyGroup(key : Int) : Boolean  = {
    if (belongsToMe(key)) return true;
    var cur = nextServer;
    var x = 0;
    for (x <-0 to n){
      if (cur.belongsToMe(key)) return true;
      cur = cur.nextServer;
    }
    return false;
  }

  def connect(network : Server): Unit = { 
    // Here we need to split the data space between this Server and the existingServer.
    nextServer = network.findSuccessor(id);
    prevServer = nextServer.prevServer;

    prevServer.nextServer = this;
    nextServer.prevServer = this;
  }
}
