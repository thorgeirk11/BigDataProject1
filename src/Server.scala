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
  var updatedNeeded : Boolean = false;

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
    if (belongsToMe(key)) return this;
    else return nextServer.findSuccessor(key);
  }
  def belongsToMe(key : Int) : Boolean = {
    if (prevServer.getServerId() == id) return true;
    if (id >= key && prevServer.getServerId() < key) return true;
    
    if (id < prevServer.getServerId()){
      // the bridge.
      if (key < id || key > prevServer.getServerId()) return true;
    }
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

  // Connect a new node into a network.
  def connect(newServer : Server): Server = {
    var next = findSuccessor(newServer.getServerId());
    var prev = next.prevServer;

    newServer.nextServer = next;
    newServer.prevServer = prev;

    prev.nextServer = newServer;
    next.prevServer = newServer;
    
    //connectDataInit();
    //prevServer.UpdateData();
    return newServer;
  }

  private  def connectDataInit() : Unit = {
      var cur = prevServer;
      var x = 0;
      for (x <- 0 to n+1){
        cur.updatedNeeded = true;
        cur = cur.nextServer;
      }
      prevServer.UpdateData();

      cur = this;
      for (x <- 0 to n) {
        cur = cur.prevServer;
        for ((k,v) <- cur.dataMap)
          writeData(k,v);
      }
  }

  def UpdateData() : Unit = {
    if (updatedNeeded)
    {
      var cur = this;
      var x = 0;
      for (x <- 0 to n) {
        cur = cur.prevServer;
      }

      var minId = cur.getServerId();
      dataMap = dataMap.retain((k,v) => k > minId)
      updatedNeeded = false;

      nextServer.UpdateData();
    }
  }
}
