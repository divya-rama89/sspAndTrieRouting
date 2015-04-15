


import java.io.*;
import java.util.*;

// Fibonacci heap implementation
public class fibHeap {

	TreeNode min ;
	LinkedList<TreeNode> heap;
	Hashtable<Integer,TreeNode> deg = new Hashtable<Integer,TreeNode>();
	
	public Boolean isEmpty() {
		if(heap.size() == 0 || heap == null){
			return true;
		}
		else{
			return false;
		}
	}
	
	// construct linkedlist of trees
	// insert at root level
	// nodeNumber = reference for the node as vertexNumber
	// nodeVal = the distance value it holds as data
	public void insert(int nodeNumber, int nodeVal){
		insert(new TreeNode(nodeNumber, nodeVal));	    
	}
	
	//construct linkedlist of trees
	// Insertion of a treeNode at the root level
	// left, right, parent fields need to be reset. childCut needs to be reset though not meaningful at root level
	public	void insert(TreeNode x){
		if(x == null) {
			return;
		}	
		if(heap == null) {
				heap = new LinkedList<TreeNode>();
			}
			x.left = null;
			x.right = null;
			x.childCut = false;
			x.parent = null;
			//System.out.println("inside insert"+x.data);
			if(heap.size() == 0) {
				
				heap.add(x);
				min = x;
			
			}
			else {
				TreeNode temp = null;
				
				if(heap.getLast() != null) {
					temp = heap.getLast();
					temp.left = x;
					x.right = temp;
					
				}
				heap.add(x);
				
			//	System.out.println("temp = "+heap.getFirst().data+" "+heap.getLast().data);
				//TODO:
				if (x.data < min.data) {
					min = x;
				}
			}
			
		}
		
	//To search for a node by its vertexNumber if we do not have the direct treeNode reference
		public TreeNode find(int vNum){
			if(heap == null || heap.size()==0){
				return null;
			}
			
			for(TreeNode e:heap){
				if(e.vertexNumber == vNum){
					return e;
				}
				else{
					if(e.children.size() != 0) {
						TreeNode result = lookInChildren(e, vNum);
						if(result != null) {
							return result; 				
						}
					}
				}
			}
			return null;
		}
		
		// This function aids searching; Search a node with vertexNumber = vNum inside the children of node e
		public TreeNode lookInChildren(TreeNode e, int vNum) {
			if(e == null) {
				return null;
			}
			if(e.children.size() != 0) {
					for (TreeNode x:e.children) {
						if(x.vertexNumber == vNum)
							return x;
						else {
							TreeNode result = lookInChildren(x, vNum);
						
							if(result != null) {
								return result; 				
							} //else dont do anything
						}
					}	//check for next element
				}
			return null;
		}
		
		// decreaseKey reduces the data value of a node with vertexNumber vNum by amount 
		// Returns true if the key was found and the decreasing was valid
		public Boolean decreaseKey(int vNum, int amount){
			TreeNode x=find(vNum);
			
			if(x == null) {
				return false;
			}
			
				return decreaseKey(x, amount);
		}
		
		// decreaseKey function where we directly pass the node reference
		// Returns true if the key was found and the decreasing was valid
		public Boolean decreaseKey(TreeNode x, int amount) {
		if(x == null) {
			return false;
		}
		
		int temp = x.data;
		if((temp - amount) < 0) { // && !forDel){
			return false;
		}
		x.data = temp - amount;
		//System.out.println("new data ="+x.data);
		if(x.parent != null) {
			if(x.data < x.parent.data) {
				CascadeCut(x);
			}
		}
		
		//min updation is taken care of in insertion; need not update again
		return true;
		}
	
	//remove node x from root level
	public void removeFromTopLevel(TreeNode x) {
		if(x == null) {
			return;
		}
		//handle left right part
		if(x.left != null) {
			TreeNode temp = x.left;
		
			if(x.right != null) {
				temp.right = x.right;
				temp.right.left = x.left;
			}
			else{
				x.left.right = null;
			}
		}
		else {	
			if(x.right != null) {
				x.right.left = null;			
			}	
		}
		
		//clear from degree table cos called from in between meld
		if(deg.get(x.degree)==x) {
			deg.remove(x.degree);
		}
		
		heap.remove(x);
		
		//updates the min value if the node removed from root level was the minimum
		//this is true when we are removing min
		if(x==min) {
			updateMin();
		}
	}
	
	//updates the minimum value - iterates through root level to search for minimum value
	public void updateMin() {
		if(heap == null || heap.size() == 0){
			min = null;
		}
		else {
			min = heap.getFirst();
			for(TreeNode x: heap){
				if (x.data < min.data) {
					min = x;
				}
			}
		}
	}
	
	
	// removes and returns the minimum value node
	public TreeNode removeMin() {
		//Meld and change min pointer
		if(this.min == null) {
			return null;
		}
		TreeNode x = this.min;
		
		// remove from root level
		removeFromTopLevel(x);
		
		// insert children into root level
		if(x.children != null) {
		//	System.out.println(x.children.size());
			//disconnect children
			for(TreeNode e:x.children) {
				//System.out.println("found child..."+e.data+" "+x.children.size());
				
				insert(e);
			}
		}
		
		// meld - join equal degree trees
		meld();
		
		return x;
	}
	
	// returns the minimum value data
	public int getMin() {
		if(this.min != null) {
			return this.min.data;
		}
		return -1;
	}
	
	// cascade cut operation for a node x
	// child will be delinked from parent
	// if the parent of x has childCut = false, it will be set to true
	// if the parent of x has childCut = true, it will also be cut i.e. we will call cascade cut for the parent
	// The above two condition is done recursively until a parent with childCut = false is encountered. 
	public void CascadeCut(TreeNode x) {
		if(x == null) {
			return;
		}
		if(x.parent != null) {
			TreeNode par = x.parent;
			removeFromChildrenList(par, x);
			
			if(par.childCut == false) {
				par.childCut = true;
				insert(x);
			}
			else {
				//cut off the tree and keep doing until u meet a childCut false parent
				insert(x);
				x.parent = null;
				CascadeCut(par);
			}
		}		
	}
	
	//Delink the child from the parent node
	public void removeFromChildrenList(TreeNode par, TreeNode child) {
		if(par == null || child == null) {
			return;
		}
		// if only one child
		if(par.children.size() == 1) {
			TreeNode temp = par.children.get(0);
			if(temp == child) {
			//no need to adjust child left/right pointers
			}
		}
		else {
			//more than one child
			if(child.left != null) {
				TreeNode temp = child.left;
				if(child.right != null) {
					child.right.left = temp;
					temp.right = child.right;
				}
				else {
					temp.right = null;
				}
			}
			else {
				if(child.right != null) {
					child.right.left = null;
				}
			}
		}
		par.children.remove(child);
		par.degree--;
	}
	
	// Add the child node to the parent 
	public void addToChildrenList(TreeNode par, TreeNode child) {
		if(par == null || child == null) {
			return;
		}
		if(par.children.size() == 0) {
			child.left = null;
			child.right = null;
		}
		else {
			TreeNode temp = par.children.get(par.children.size()-1);
			temp.right = child;
			child.left = temp;
		}
		child.parent = par;
		par.children.add(child);
		// check if needed
		// par.chilCut = false;
		child.childCut = false;
		par.degree++;
	}
	
	// Melds equal degree trees in the heap so that not more than one tree exists for each degree
	public void meld() {
		//int iter = 0;
		//Maintain hashTable of degree
		deg.clear();
		
		while (deg.size() != heap.size()) {
		//iter++;
			//System.out.println("inside meld round#"+ iter);
				
		{			
		
		TreeNode e = heap.getFirst();
		//System.out.println("first element ="+e.data);
		TreeNode next = null;
		while (e != null) {
			int oldDeg = e.degree;
			next = e.left;
		//	System.out.println("inside meld1...not in deg..e.data and deg="+e.data+e.degree);
			 
			//a tree corresponding to degree already exists
			if(deg.containsKey(oldDeg)) {
				// then degree of key not 0
				TreeNode temp = deg.get(e.degree);
				
				if(e.data < temp.data) {
					removeFromTopLevel(temp);
					addToChildrenList(e,temp);
					//doing 2nd time
					deg.remove(oldDeg);
					if(!deg.containsKey(e.degree)) {
						deg.put(e.degree, e);
					}
				}
				else {
					if(temp != e) {
						removeFromTopLevel(e);
						addToChildrenList(temp,e);
						//doing 2nd time
						deg.remove(oldDeg);
						if(!deg.containsKey(temp.degree)) {
							deg.put(temp.degree, temp);
					}
					}
				}
			} 
				else {
					// if no node corresponding to this degree exists, this node is recorded in the degree tree
					deg.put(oldDeg, e);
				}	
				e = next;
			}
		}
		
	}
	//updateMin();
}

	// for testing purposes - display the root level nodes' data
	public void displayTopHeap() {
		System.out.println("Top level list");
		if(heap == null || heap.size()==0){
			return;
		}
		TreeNode e = heap.getFirst();
		while(e!=null) {
			System.out.print(" "+e.data);//+" "+e.left.data+" "+e.right.data);
			e=e.left;
		}
		System.out.print("------");
		for(TreeNode x:heap) {
			System.out.print(": "+ x.data);
		}
	}
	
	//for displaying all level nodes' data - displays the data in the given node and the node's children
	public void displayList(List<TreeNode> x) {
		if(x == null) {
			return;
		}
		if(x.size() == 0) {
			return;
		}
		for (int i = 0; i < x.size(); i++) {
			TreeNode j = x.get(i);
			System.out.println("data, parent, childcut ="+j.data+" "+j.parent.data+" "+j.childCut+" ");
			if(j.children.size() != 0){
				displayList(j.children);
			}
		}
	}
	
	//for testing purposes - display all nodes' data
	public void displayFull() {
		System.out.println("full list");
		if(heap == null || heap.size()==0){
			return;
		}
		TreeNode e = heap.getFirst();
		while(e!=null) {
			System.out.print(" data= "+e.data+" cc ="+e.childCut);//+" "+e.left.data+" "+e.right.data);
			
			if(e.children.size() != 0){
				displayList(e.children);
			}
			e=e.left;
		}
	
	}
	
public void test2() {
		
		int[] arr = {8,7,5,30};
		for (int i = 0; i < arr.length; i++) {
			insert(arr[i],arr[i]);
		}
		displayFull();
		meld();
		System.out.println("min value="+getMin()+" after meld ");
		displayFull();
}

    public void test3() {
    	insert(3,3);
		displayTopHeap();
		insert(4,4);
		displayTopHeap();
		//System.out.println(x.getMin());
		insert(2,2);
		displayTopHeap();

		//x.deleteKey(4);
		//x.displayTopHeap();
		//System.out.println(x.getMin());
		insert(5,5);
	    displayTopHeap();
	    //x.meld();
	    
	    displayFull();
		removeMin();
		displayTopHeap();
		System.out.println("min value"+getMin());
		
		removeMin();
		System.out.println("min value"+getMin());
		displayTopHeap();
		
		decreaseKey(4,3);
		System.out.println("after decrease key value"+getMin());
		//System.out.println(x.removeMin().data);
		//System.out.println(x.removeMin().data);
		//deleteKey(1);
		displayTopHeap();
		//System.out.println("------------------");
		
		//System.out.println("min value = "+x.getMin()); 
    }

    public void test(){
    	Random rand = new Random();
    	ArrayList<Integer> mylist=new ArrayList<Integer>(); 
    	for (int i = 0; i < 1000; i++) {
    		Integer x = rand.nextInt(100);
    		insert(x,i);
    		mylist.add(i, i);
    	}
    	System.out.println("---------------------------------------------inserted-------------------------------------------");
    	displayTopHeap();
    	removeMin();
    	displayTopHeap();
    	System.out.println("get min "+getMin());
    	
    	for (int i = 0; i < 100; i++) {
			int y = rand.nextInt(mylist.size());
			decreaseKey(mylist.get(y), i);
			removeMin();
			displayTopHeap();
			System.out.println("get min "+getMin());
		}
    }
    
	public void test1() {
		
		int[] arr = {0, 0,0,0,15,7,8,6,4,3,30};
		for (int i = 0; i < arr.length; i++) {
			insert(arr[i],arr[i]);
		}
		
		System.out.println("min value="+getMin()+" after insert ");
		displayFull();
		displayTopHeap();
		removeMin();
		System.out.println("after removeMin). min value="+getMin());
		displayFull();
		displayTopHeap();
		
		insert(22,22);
		insert(12,12);
		insert(1,1);
		System.out.println("after inserting(1,12,22). min value="+getMin());
		
		removeMin();
		System.out.println("after removeMin). min value="+getMin());
		displayFull();
		displayTopHeap();
		
		
		decreaseKey(15, 8);
		System.out.println("after decreaseKey(15,8). min value="+getMin());
		displayFull();
		displayTopHeap();
		decreaseKey(8, 3);
		System.out.println("after decreaseKey(8,3). min value="+getMin());
		displayFull();
		displayTopHeap();
		decreaseKey(8, 8);
		System.out.println("after decreaseKey(8,8). min value="+getMin());
		displayFull();
		displayTopHeap();
		decreaseKey(30, 15);
		System.out.println("after decreaseKey(30,15). min value="+getMin());
		displayFull();
		displayTopHeap();
		
		
		
		
				
		removeMin();
        System.out.println("after removeMin. min value="+getMin());
        displayFull();
        displayTopHeap();


        decreaseKey(5,3);
		System.out.println("after decreaseKey(5,3). min value="+getMin());
		displayFull();
		displayTopHeap();
        
        for (int i = 0; i <9; i++) {
        	removeMin();
            System.out.println("after removeMin. min value="+getMin());
            displayFull();
            displayTopHeap();	
		}
        

        
	}
	
	public static void main(String[] args){		
		
		fibHeap x = new fibHeap();
		//x.test2();
		//System.out.println("----------------------");
		x.test1();
				
	}
	
}
