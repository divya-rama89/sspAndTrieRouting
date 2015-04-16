import java.io.*;
import java.util.*;

class Result{
	ArrayList <Integer> path;
	int weight;
	
	public Result(ArrayList<Integer> p, int w) {
		this.path = p;
		this.weight =w;
	}
	
}

public class ssp {
	static graph graphx;
	//static int numVer = 0;
	//static int numEdg = 0;
	
	/*/////////////////////////////////////////
	public static void init(fibHeap vertexHeap, TreeNode source){
		//fill in queue initially with all nodes
        //System.out.println("called init with "+source.data);
		source.data = 0;
		for(TreeNode x:graphx.vertexList){
        	
        	x.previous = null;
        	
        	vertexHeap.insert(x);
        	//System.out.println("Inserted"+x.vertexNumber+" "+x.data);
        }
		
	}*/
	    
	public static void init(PriorityQueue<TreeNode> vertexHeap, TreeNode source){
		//fill in queue initially with all nodes
        //System.out.println("called init with "+source.data);
		
		for(TreeNode x:graphx.vertexList){
        	if(x!=source) {
        		x.data=Integer.MAX_VALUE;
        	}
        	else {
        		x.data = 0;
        	}
        	x.previous = null;
        	
        	vertexHeap.add(x);
        	//System.out.println("Inserted"+x.vertexNumber+" "+x.data);
        }
		
	}
	
	
	public static void findPath(TreeNode source)
    {
    	//System.out.println("inside computePaths for "+source.vertexNumber);
    	source.data = 0;
    	
        /////////////////////
    	//fibHeap vertexHeap = new fibHeap();
    	 PriorityQueue<TreeNode> vertexHeap = new PriorityQueue<TreeNode>();
        
        init(vertexHeap, source);
        
	while (!vertexHeap.isEmpty()) {
		TreeNode u = vertexHeap.remove(); //removeMin for heap
		//System.out.println("Polled out "+u.data);
            // Visit each edge exiting u
            for (Edge e : u.adj)
            {
            	TreeNode v = e.dest;
            	
                int weight = e.weight;
                int fullDist = u.data + weight;
                if (fullDist < v.data) {
                	////////////
                	//int diff = v.data-fullDist;
                	//System.out.println("old value of data ="+v.data+" reducing by "+diff);
                	
                	//vertexHeap.decreaseKey(v,diff);
///////////////////////////                	
                	
                	vertexHeap.remove(v);
                	v.data = fullDist ;
                	//System.out.println("setting prev neighbour to "+u.vertexNumber);
                	v.previous = u;
                	vertexHeap.add(v);
		           
                	}
                else{
                	//nothing
                }
            }
        }
	
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
	
    public static Result getssp(TreeNode src, TreeNode dest) {
    	if(src==dest){
    		return new Result(new ArrayList<Integer>(src.vertexNumber), 0);
    	}
    	ssp.findPath(src);
				
		// get the path to the required destination node
		List<TreeNode> path = ssp.getPath(dest);
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = 0; i < path.size(); i++) {
			
			temp.add(i, path.get(i).vertexNumber);
		   // System.out.println(" "+temp.get(i));
		}
		
		return new Result(temp, dest.data);			
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
				in.close();
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
			
			TreeNode srcNode = graphx.vertexList.get(src);
			TreeNode destNode = graphx.vertexList.get(dest);
			Result answer = getssp(srcNode, destNode);    
			
			//displaying result
			System.out.println(answer.weight);
			
			// displaying the path
			//System.out.print(" Path: ");
			for (int i = 0; i < answer.path.size(); i++) {
			    	System.out.print(answer.path.get(i)+" ");
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