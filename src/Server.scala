/**
  * Created by centos on 1/26/17.
  */
  
// Id = hash id for this server
// n = data replication factor 
// c = how many unique keys in the circle
class Server(id: Int, n: Int, c: Int)  {
  var writeCount = 0;
  var dataMap = collection.mutable.Map[Int, String]();
  var fingerTable = new Array[Server](math.floor(math.log10(c) / math.log10(2.0)).toInt);;

  var nextServer : Server = this;
  var prevServer : Server = this;
  var updatedNeeded : Boolean = false;

  def getServerId() : Int = {
    return id;
  }

  def buildFingerTable() : Unit ={
    var curId = id;
    var i = 0; 
    for (i <- 1 to fingerTable.size){
      curId = math.floor((curId + math.pow(2,1)) %  c).toInt;
      fingerTable(i) = findSuccessor(curId);
    }
  }

  /**
  def closest_preceding_finger(id) : Unit = {
    for (i = m to 1)
      if (fingerTable[i].getServerId() )
  }

  */

  def get(key: Int) : String = {
    if (belongsToMyGroup(key)) return dataMap(key);
    else return nextServer.get(key);
  }
  def put(key: Int, data : String) : Unit = {
    if (belongsToMe(key))
    {
      var cur = this;
      var x = 0;
      for (x <- 1 to n){
        cur.writeData(key,data);
        cur = cur.nextServer;
      }
    }
    else nextServer.put(key,data);
  }
  private def writeData(key:Int, data:String) :Unit = {
    writeCount+=1;
    dataMap(key) = data;
  }
  
  private def findSuccessor(key : Int) : Server  = {
    if (belongsToMe(key)) return this;
    else return nextServer.findSuccessor(key);
  }
  private def belongsToMe(key : Int) : Boolean = {
    if (prevServer.getServerId() == id) return true;
    if (id == key) return true;
    if (id > key && prevServer.getServerId() < key) return true;
    
    if (id < prevServer.getServerId()){
      if (key < id || key > prevServer.getServerId()) return true;
    }
    return false;
  }
  private def belongsToMyGroup(key : Int) : Boolean  = {
    if (belongsToMe(key)) return true;
    var cur = prevServer;
    var x = 0;
    for (x <- 1 to n){
      if (cur.belongsToMe(key)) return true;
      cur = cur.prevServer;
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
    
    newServer.connectDataInit();
 
   
    return this;
  }

  private  def connectDataInit() : Unit = {
      var cur = nextServer;
      var x = 0;
      for (x <- 1 to n){
        cur.updatedNeeded = true;
        cur = cur.nextServer;
      }
      nextServer.UpdateData();

      cur = nextServer;
      for (x <- 0 to n+1) {
        for ((k,v) <- cur.dataMap) {
          if (belongsToMyGroup(k))
            writeData(k,v)
        }
        cur = cur.prevServer;
      }

      buildFingerTable();
  }

  private def UpdateData() : Unit = {
    if (updatedNeeded)
    {
      var cur = this;
      var x = 0;
      for (x <- 1 to n) {
        cur = cur.prevServer;
      }

      var minId = cur.getServerId();
      dataMap = dataMap.retain((k,v) => k > minId)
      updatedNeeded = false;

      nextServer.UpdateData();
    }
  }
}
