/**
  * Created by centos on 1/26/17.
  */
  
// Id = hash id for this server.
// n = how many clones 
class Server(id: Int, n: Int) {
  private var writeCount : Int;
  private var datamap : collection.mutable.Map[Int, String];

  var nextServer : Server = this;
  var prevServer : Server = this;
  
  def get(key: Int) : String { 
    if (belongsToMyGroup(key)) return data[key]; 
    else return nextServer.get(key);
  }
  def put(key: Int, data : String) { 
    if (belongsToMe(key))
    {
      var cur = this;
      for (var x <- 0 to n){
        cur.writeData(key,data);
        cur = cur.nextServer;
      }
    }
    else nextServer.put(key,data);
  }
  def writeData(key:Int, data:String){
    writeCount++;
    datamap[key] = data;
  }
  
  def findSuccessor(key : Int) : Server {
    if (belongs(key)) return nextServer;
    else return nextServer.findSuccessor(key);
  }
  def belongsToMe(key : Int) : Boolean {
    if (key == id) return true;
    if (id > nextServer.id){ 
      // the bridge.
      if (key > id || key < nextServer.id ) return true;
    }
    if (id < key && nextServer.id > key) return true;

    return false;
  }
  def belongsToMyGroup(key : Int) : Boolean {
    if (belongsToMe(key)) return true;
    var cur = nextServer;
    for (var x <- 0 to n){
      if (cur.belongsToMe(key)) return true;
      cur = cur.nextServer;
    }
  }

  def connect(network : Server): Unit = { 
    // Here we need to split the data space between this Server and the existingServer.
    nextServer = network.findSuccessor(id);
    prevServer = nextServer.prevServer;

    prevServer.nextServer = this;
    nextServer.prevServer = this;
  }
}
