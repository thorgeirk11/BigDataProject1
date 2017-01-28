Distrubuted Hashtable Projcet.

We implemented a circle based system, similar to chord.

Server class is the nodes in the network. At their core is a doubly
linked list, each server has a next and prev link. Each server stores
data for the key space between n previous server id and its own id, 
where n is the replication factor. 

When connecting the servers they automatically adjust the network,
there is therefore no need to call a special init() function on the 
network.

We implemented a overlay network where each server has a lookup table 
for other servers on the circle. Similar to chord, the finger table is
populated with floor(log_2(c/2)) entries. Where the i-th entry is the 
successor of the id+2^i key on the circle. This ensures that a lookup
of a key takes log_2(n) for a network of size n.

Our implementation has two versions of get and put, one that uses the 
finger table for lookup and the other uses the doubly linked list. In
our experiments we show the efficency of each.


The main funciton takes in parameters:
  -s Number of initial servers in the network. Default is 10.
  -e How large is the circle (Key space). Default 10000.
  -n Data replication factor. Default 3.
  -w How many write operations should be simulated. Default 1000000.
  -i How many servers are added to the network at each step. Default 5.
  -sm How many servers are at the end of the simulation. Default 30.
  
AUTHORS	
    Ingibergur Sindri Stefnisson 
    Sigurgrímur Unnar Ólafsson
    Þorgeir Auðunn Karlsson 
    
