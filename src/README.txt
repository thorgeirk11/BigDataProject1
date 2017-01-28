Distrubuted Hashtable Projcet.

We implemented a circle based system, similar to cord.

Server class is the nodes in the network. At their core is a doubly
linked list, each server has a next and prev link. Each server stores
data for the key space between n previous server id and its own id, 
where n is the replication factor. 

When connecting the servers they automatically adjust the network,
there is therefore no need to call a special init() function on the 
network.







README	General information


THANKS	Acknowledgments

CHANGELOG	A detailed changelog, intended for programmers

NEWS	A basic changelog, intended for users



The main funciton takes in parameters:
  -s Number of intial servers in the network. Defualt is 10.
  -e How large is the circle (Key space). Defualt 10000.
  -n Data replacation factor. Defualt 3.
  -w How many write operations should be simulated. Defualt 1000000.
  -i How many servers are added to the network at each step. Defualt 5.
  -sm How many servers are at the end of the simulation. Default 30.


  
AUTHORS	
    Ingibergur Sindri Stefnisson 
    Sigurgrímur Unnar Ólafsson
    Þorgeir Auðunn Karlsson 
    