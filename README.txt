README.txt for Programming Component 1
Tom Shaw
Cs455

####################################################################################################

Information:

This is my implementation of a simplified Distributed Hash Table. The main concept is to get a number of messaging nodes to communicate in a round robin fashion that enables the nodes to communicate at times indirectly. This is accomplished by every messaging node communicating with a central registry. On startup each messaging node will attempt to register themselves with the registry. After a few nodes have registered the registry can then create an overlay which will give every Messaging node the complete list of nodes in the system, and ip and port information for nodes that are varying distances away. The registry can then be instructed to start an experiment which will instruct the messaging nodes to send a number of random integers to random nodes in the system. Note that each node will not have information to communicate with every other node, to solve this problem nodes will route packets as close to the destination node as they can. After sending all meesage the nodess will inform the registry who will ensure the correctness of all the messages sent.
There are two Mains found in this repo, one in Registry and one in Messaging Node. Both are meant to be running concurrently, with a single instance of Registry running and multiple versions of Messaging Nodes running.

####################################################################################################

Important information:

NOTICE!!! The included project has all the packages stored within a src directory. These programs have only been tested using the run commands from within the src directory. If problems occur with testing please attempt using commands from this directory. 

Included is a makefile that only has the two operations "make all" and "make clean". NOTE this file is meant to be used outside the src directory of the project.

Proper usage of the program involves using any of the commands that are found in the assignment description. In addition the programs Registry and Messaging Node must be started using the commands given in section 5.

In order to gracefully exit the program please use the exit-overlay command on each messaging node which will deregister and close the programs. In order to close the Registry program gracefully the command "exit" will close all connections and the node itself. NOTE: this does not close the messaging nodes, so the messaging nodes should be closed before exiting the registry.

####################################################################################################

Description of Classes:

Registry: this class contains of the two mains included in this project. The purpose of registry is to Coordinate and control the messaging nodes in the system. In addition Registry maintains information about how each class communicates with eachother.

Messaging Node: this Class contains the second main found in this project. The purpose of messaging node is to communicate with other nodes in the system and maintain a count of these communications that can be later viewed from the Registry.

Node: The Node class is the super class of Messaging node and Registry. It contains some state variables and a small contructor.

TCPConnectionCache: The purpose of this class is to provide a means of storing a variable number of connections and provide some measure of control over all connections safely. It also manages a TCPServerThread that stores new incomming connections.

TCPConnection: The purpose of this class is to store a TCPSender and TCPReceiver relating to communications with a single node whether that is the registry or another node.

TCPSender: This class has the sole responsibility of managing outgoing messages going to a single node in relation to a single node.

TCPReceiver: This class has the sole responsibility of managing incoming messages coming from a specific source and putting it into an event queue that is used by the registry to perform actions.

TCPServerThread: This class is effectively a wrapper class for a ServerSocket. It creates itself whether on a random open port or on a given port, and waits for new incoming connections.

RoutingTable: A storing mechanism that holds Routing Entries and provides the logic for where to send packets.

RoutingEntry: this class simply holds info about a TCPConnection for use in the Routing Table.

RgistryTable: a more robust RoutingTable that is used by the Registry to store and handle its connections.

Registry Entry: a more robust RoutingEntry that stores additional information about each connection needed by the Registry.

Parser: A superclass that contains some information about scanners and nodes.

RegistryParser: a simple parser that handles input commands for the Registry.

MessagingParser: a simple Parser that handles input commands for the Messaging nodes.

Event: an interface that has the methods getType() and getBytes() that are needed by each wireformat.

EventFactory: A clas with a single static method that takes an event and determines which type it is and constructs the proper object.

Protocol: this class is a super class for each wireformat and contains the constants used to determine the type of message. Also has two methods marshallbytes and unmarshallbytes.

The rest of the classes in wireformats not yet discussed: these classes store only the necessary information required for proper communication between nodes. They contain two constructors one used by the sender and one used by the receiver, and have getters for each elements stored in the message.
