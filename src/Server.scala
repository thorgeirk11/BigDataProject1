/**
  * Created by centos on 1/26/17.
  */
  
// Id = hash id for this server
// n = data replication factor 
// c = how many unique keys in the circle
class Server(id: Int, n: Int, c: Int)  {
  var writeCount = 0;
  var messageCount = 0;
  var dataMap = collection.mutable.Map[Int, String]();
  var fingerTable = new Array[Server](0);

  var nextServer : Server = this;
  var prevServer : Server = this;
  var dataUpdateNeeded : Boolean = false;

  def getServerId() : Int = id;

  def buildFingerTable() : Unit = {
    var size = math.floor(math.log10(c/2f) / math.log10(2.0)).toInt;
    fingerTable = new Array[Server](size);
    UpdateFingerTable();
    
    //Update fingertables 
    prevServer.UpdateFingerTable();
    var prevServerId = prevServer.getServerId();
    for (i <- 0 to size-1) {
      var fingerId = java.lang.Math.floorMod((prevServerId - math.pow(2,i+1)).toInt,  c);
      findSuccessor(fingerId).UpdateFingerTable();
    }
  }

  def UpdateFingerTable() : Unit ={
    for (i <- 0 to fingerTable.length-1) {
      var fingerId = java.lang.Math.floorMod((id + math.pow(2,i+1)).toInt,  c);
      fingerTable(i) = findSuccessor(fingerId);
    }
  }

  def getWithFinger(key:Int) : String = {
    messageCount+=1;
    println(id);
    if (dataMap.contains(key)) return dataMap(key); 
    if (belongsToMe(key)) return null;
    
    var n_key = key;
    if (key < id) n_key = key + c;
    var closest = fingerTable.minBy(s => if (s.getServerId() < id) 
                                           math.abs( c + s.getServerId() - n_key) 
                                         else 
                                           math.abs( s.getServerId() - n_key));
    
    if (closest.getServerId() < key)
      return closest.nextServer.getWithFinger(key);
    else
      return closest.getWithFinger(key);
  }
  
  def get(key: Int) : String = {
    messageCount += 1;
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
    newServer.buildFingerTable();

    return this;
  }

  private  def connectDataInit() : Unit = {
      var cur = nextServer;
      var x = 0;
      for (x <- 1 to n){
        cur.dataUpdateNeeded = true;
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

  }

  private def UpdateData() : Unit = {
    if (dataUpdateNeeded)
    {
      var cur = this;
      var x = 0;
      for (x <- 1 to n) {
        cur = cur.prevServer;
      }

      var minId = cur.getServerId();
      dataMap = dataMap.retain((k,v) => k > minId)
      dataUpdateNeeded = false;

      nextServer.UpdateData();
    }
  }
}
