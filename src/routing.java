import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;


public class routing {
	static graph graphx;
	// to sore the IPs in their binary forms
	static ArrayList<String> listIPs = new ArrayList<String>();
	
	//static int numVer = 0;
	//static int numEdg = 0;
	
	
	// Referred to http://stackoverflow.com/questions/9900284/converting-an-ip-address-to-binary-values-java for this function
			public static String convertIPtoBinary(String data_in) {
				
		        byte[] bytes = null;
				try {
					bytes = InetAddress.getByName(data_in).getAddress();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		       return new BigInteger(1, bytes).toString(2);
			}
			
	
  public static void main(String[] args) {
		  
	  
	int cntChk = 0;
	
	if(args.length < 4){
		System.out.println("Not enough arguments :\n ssp <routing filename1> <filename2> <source> <destination> ");
		return;
	}

	String routingFilename = args[0];
	String filename2 = args[1];
	int src = Integer.parseInt(args[2]);
	int dest = Integer.parseInt(args[3]);
	
	try {
		Scanner	in = new Scanner(new FileReader(routingFilename));
		
		if(in.hasNext()){
			int numVer = in.nextInt();
			
			if(src > numVer || src < 0 || dest >numVer || dest <0){
				System.out.println("Invalid input");
				in.close();
				return;
			}
			
			// use ssp's graph object
			graphx = new graph(numVer);
			graphx.numVer = numVer;
			System.out.println("numver:"+numVer);
			graphx.numEdg = in.nextInt();
			System.out.println("numedge" + graphx.numEdg);
			
			for (int i = 0; i < graphx.vertexList.size(); i++) {
				System.out.println("vertex->"+ graphx.vertexList.get(i).data);
			}
			
			while(in.hasNext()){
				graphx.addEdge(in.nextInt(), in.nextInt(), in.nextInt());
				cntChk++;
			}
			
			// if the number of edges not as specified, input marked as invalid
			if(cntChk != graphx.numEdg) {
				System.out.println("Invalid input! Program exiting!");
				in.close();
				return;
			}
			//graphx.displayAdj();
			
			// Store IP to node number mapping in listIPs	
			
						in = new Scanner(new FileReader(filename2));
						int index =0;
						while(in.hasNext()){
							String ip = in.next();
							listIPs.add(index,convertIPtoBinary(ip));
							index++;
						}
						System.out.println("index,numVer="+index+" "+numVer);
						if (index != numVer) {
							System.out.println("Invalid input-number of IPs not matching with number of vertices! Program exiting!");
							in.close();
							return;
						}
								
						/*for (int i = 0; i < numVer; i++) {
							System.out.println("value is"+i+" "+listIPs.get(i));
						}*/
						
						
						//Initialisation
						//For each vertex - 
						//Calculate shortest path to every other vertex
						//Store in in its trie the nextHop and weight value
						//Compress the trie
						TreeNode initSrc, initDest;
						//Result initResult;
						ArrayList<HashMap<String, nodeObject>> hMapList = new ArrayList<HashMap<String, nodeObject>>();
						
						//initialise the hashmap routing list
						for (int i = 0; i < numVer; i++) {
							HashMap<String, nodeObject> inithMap = new HashMap<String, nodeObject>();
							hMapList.add(i,inithMap);
						}
						
						ssp.graphx=graphx;
						//int i =0;
						//int j=4;
						for (int i = 0; i < numVer; i++) 
						{
							//find the shortest paths
							initSrc = graphx.vertexList.get(i); 
							//System.out.println(initSrc.vertexNumber);					
							for (int j = i; j < numVer; j++) 
							{
								//retreive the next hop for each destination and store in trie at appropriate location
								initDest = graphx.vertexList.get(j);
								//System.out.println(initDest.vertexNumber);
																
								Result initResult = ssp.getssp(initSrc,initDest);
								System.out.println(i+","+j);
								for (int j2 = 0; j2 < initResult.path.size(); j2++) {
									System.out.println(initResult.path.get(j2));
								}
								
								if(i!=j) {
									// the 1 on the path will be destination or next hop
									Integer x = initResult.path.get(1);
									//get the IP address of the destination in order to store it at the correct location on the trie
									String vertexName = listIPs.get(j);
								
									//store in hashmaps
									//To store, key will be the address in binary and next hop will be int
									
									//In that of the starting node...
									HashMap<String, nodeObject> abc = hMapList.get(i);
									abc.put(vertexName, new nodeObject(x.toString(),initResult.weight));
									hMapList.add(i,abc); 	
									
									//In that of the ending node
									x = initResult.path.get(initResult.path.size()-2);
									vertexName = listIPs.get(i);
									abc = hMapList.get(j);
									abc.put(vertexName, new nodeObject(x.toString(),initResult.weight));
									hMapList.add(j,abc); 	
								}
							}
						}// at this point, all data to be filled in trie is ready
						
						
						// create and compress the tries
						for ( int i = 0; i < numVer; i++) {
							HashMap<String, nodeObject> trieMap = hMapList.get(i);
							TreeNode temp = graphx.vertexList.get(i);
							temp.routerTrie= new BinaryTrie(trieMap);
							temp.routerTrie.compressTrie();
						}// at this point, all tries are ready
						
						System.out.println("tries populated");
						
						//for given source and destination, report the shortest path
						ArrayList<Integer> resultPath = new ArrayList<Integer>();
						TreeNode srcNode = graphx.vertexList.get(src);
						TreeNode destNode = graphx.vertexList.get(dest);
						System.out.println("src and dest  = "+srcNode.vertexNumber+" "+destNode.vertexNumber );
						TreeNode temp = srcNode;
						int totalWeight = 0;
						String[] totalPath = null;
						int ctr = 0;
						while(temp != destNode){
							resultPath.add(temp.vertexNumber);
							String destIP = listIPs.get(destNode.vertexNumber);
							Prefix tempResult = temp.routerTrie.getNextHopNodeAndPrefix(destIP);
							totalWeight += tempResult.weight;
							totalPath[ctr]=tempResult.nodeName;
							ctr++;
							temp = graphx.vertexList.get(Integer.parseInt(tempResult.nextHopNode));	
						}
						
						//adding for the destination node
						totalPath[ctr]= listIPs.get(dest);
						
						//display
						System.out.println(totalWeight);
						for (int i = 0; i < totalPath.length; i++) {
							System.out.println(" "+totalPath[i]);
						}
							    
	    
			
			in.close();
		}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
  		}
  
}
