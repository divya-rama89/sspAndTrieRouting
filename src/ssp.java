

import java.io.*;
import java.util.*;


public class ssp {
	static graph graphx;
	//static int numVer = 0;
	//static int numEdg = 0;
	
	
	public static void init(fibHeap vertexHeap, TreeNode source){
		//fill in queue initially with all nodes
        //System.out.println("called init with "+source.data);
		source.data = 0;
		for(TreeNode x:graphx.vertexList){
        	
        	x.previous = null;
        	
        	vertexHeap.insert(x);
        	//System.out.println("Inserted"+x.vertexNumber+" "+x.data);
        }
		
	}
	
    public static void findPath(TreeNode source)
    {
    	//System.out.println("inside computePaths for "+source.vertexNumber);
    	source.data = 0;
        fibHeap vertexHeap = new fibHeap();
      	
        init(vertexHeap, source);
        
	while (!vertexHeap.isEmpty()) {
		TreeNode u = vertexHeap.removeMin(); //removeMin for heap
		//System.out.println("Polled out "+u.data);
            // Visit each edge exiting u
            for (Edge e : u.adj)
            {
            	TreeNode v = e.dest;
            	System.out.println("inside findPath "+v.vertexNumber);
                int weight = e.weight;
                int fullDist = u.data + weight;
                if (fullDist < v.data) {
                	int diff = v.data-fullDist;
                	System.out.println("old value of data ="+v.data+" reducing by "+diff);
                	vertexHeap.decreaseKey(v,diff);
                	// v.data = fullDist ;
                	System.out.println("setting prev neighbour to "+u.vertexNumber);
                	v.previous = u;
		           
                	}
                else{
                	System.out.println("not entered this loop");
                }
            }
        }
	System.out.println("Queue empty now!");
    }

    public static List<TreeNode> getPath(TreeNode d)
    {
        //System.out.println("inside getShortestPathto for "+d.data);
    	List<TreeNode> path = new ArrayList<TreeNode>();
        for (TreeNode vertex = d; vertex != null; vertex = vertex.previous)
            path.add(vertex);
        Collections.reverse(path);
        return path;
    }
	
	
  public static void main(String[] args) {
		  
	int cntChk = 0;
	
	if(args.length < 3){
		System.out.println("Not enough arguments :\n ssp <filename> <source> <destination> ");
		return;
	}

	String filename = args[0];
	int src = Integer.parseInt(args[1]);
	int dest = Integer.parseInt(args[2]);
	
	try {
		Scanner	in = new Scanner(new FileReader(filename));
		
		if(in.hasNext()){
			int numV = in.nextInt();
			
			if(src > numV || src < 0 || dest >numV || dest <0){
				System.out.println("Invalid input");
				return;
			}
			
			// create a graph object
			graphx = new graph(numV);
			graphx.numVer = numV;
			//System.out.println("numver:"+numVer);
			graphx.numEdg = in.nextInt();
			//System.out.println("numedge" + numEdg);
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
			
			//////////SSP
			// srcNode is the starting node
			TreeNode srcNode = graphx.vertexList.get(src);
			
			// find all paths from srcNode to every other vertex
			findPath(srcNode);
			//System.out.println("Distance from " + srcNode.data);
			    
			// destination node is the ending point
			TreeNode destNode = graphx.vertexList.get(dest);
			    
			//System.out.print("Distance to " + destNode.data + ": " + destNode.data);
	
			//displaying the weight
			System.out.println(destNode.data);
			
			// get the path to the required destination node
			List<TreeNode> path = getPath(destNode);
			
			// displaying the path
			//System.out.print(" Path: ");
			for (int i = 0; i < path.size(); i++) {
			    	System.out.print(path.get(i).vertexNumber+" ");
				}
			System.out.println();
				    
	    }
			
			in.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
 
}