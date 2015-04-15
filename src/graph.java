

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class TreeNode {

	int vertexNumber;  //vertex number
    int data;// distance which is the priority decider
    public ArrayList<Edge> adj; //to hold adjacent vertices and weight
    public TreeNode previous; // to hold the previous hop to go to source
       
    // required for heap
    Boolean childCut; //childcut field for cascadeCut operation
    TreeNode left;    // left sibling
    TreeNode right;	  // right sibling
    TreeNode parent;  
    List<TreeNode> children;
    int degree = 0;

    
    public TreeNode(int vertexNumber, int data) {
        this.vertexNumber = vertexNumber;
        this.data = data;
        this.childCut = false;
        this.children = new LinkedList<TreeNode>();
        this.adj = new ArrayList<Edge>();
    }
 }

	//Adjacency holds edge data using this data structure
 class Edge
	{
	    public final TreeNode dest;
	    public final int weight;
	    public Edge(TreeNode d, int w)
	    { 
	    	dest = d; 
	    	weight = w; 
	    }
	}

 
public class graph {
	 
	// Non changing values
	 public int numVer;
	 public int numEdg;
     public final ArrayList<TreeNode> vertexList = new ArrayList<TreeNode>();

    public graph(int Ver) {
    if (Ver < 0) throw new IllegalArgumentException("Number of vertices must be nonnegative");
    this.numVer = Ver;
    this.numEdg = 0;
    
    // build a reference list of treeNodes 
    for (int i = 0; i < numVer; i++) {
    	vertexList.add(i,new TreeNode(i, Integer.MAX_VALUE)); 	
    }
    
}

public int getVert(){
	return numVer;
}

public int getEdg(){
	return numEdg;
}

// add an edge between two nodes..update adjacency lists of both nodes
public void addEdge(int vertexAval, int vertexBval, int weight) {
	if(vertexAval < 0 || vertexBval < 0 || weight < 0) {
		return;
	}
	TreeNode nodeA =  vertexList.get(vertexAval);
	TreeNode nodeB = vertexList.get(vertexBval);
	addEdge(nodeA, nodeB, weight);
}

//add an edge between two nodes when we have node references..update adjacency lists of both nodes
public void addEdge(TreeNode vertexA, TreeNode vertexB, int weight) {
	if(vertexA == null || vertexB == null || weight < 0) {
		return;
	}
	Edge temp = new Edge(vertexB, weight);
	vertexA.adj.add(temp);
	//System.out.println("for "+vertexA.data+" "+temp.dest.data+","+temp.weight);
	
	temp = new Edge(vertexA, weight);
	vertexB.adj.add(temp);
	//System.out.println("for "+vertexB.data+" "+temp.dest.data+","+temp.weight);
}

// displays the adjacency list
public void displayAdj() {
	 for (int i = 0; i < vertexList.size(); i++) {
		 System.out.print("i="+i);	
		 TreeNode temp =vertexList.get(i);
		 for (int j = 0; j < vertexList.get(i).adj.size(); j++) {
		       Edge tempE =temp.adj.get(j);
	    	   System.out.print(" "+tempE.dest.vertexNumber + ","+tempE.weight);
	    	} 
		 System.out.println();
	    }
}

}