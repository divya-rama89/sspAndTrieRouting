import java.util.*;

class Prefix{
	String nodeName;
	String nextHopNode;
	Integer weight;
	
	public Prefix(String name, String next, Integer wt){
		this.nodeName = name;
		this.nextHopNode = next;
		this.weight = wt;
	}
}

public class BinaryTrie {

	private BinaryTrieNode root;

	public BinaryTrie(HashMap<String, nodeObject> hMap) {
		root = new BinaryTrieNode();
		for(String key : hMap.keySet()) {
			root.addNextHopNode(key, hMap.get(key));
		}
	}

	public Prefix getNextHopNodeAndPrefix(String destIPAddress) {
		System.out.println("destination IP"+destIPAddress);
		StringBuilder pre = new StringBuilder();
		if (destIPAddress == null) {
			System.out.println("entered here!!");
			return null;
		}
		BinaryTrieNode lastNode = root;
		int i = 0;
		BinaryTrieNode prevNode = null;
		for(i = 0; i < destIPAddress.length(); i++) {
			prevNode = lastNode;
			char c = destIPAddress.charAt(i);
			lastNode = lastNode.getChild(c);
			pre.append(c);
			if(lastNode == null) {
				System.out.println("did i come here");
				
				return new Prefix(pre.toString(), prevNode.firstHopNode, prevNode.weight);
			}
		}
		return new Prefix(pre.toString(), lastNode.firstHopNode, lastNode.weight);
	}
	
	public String getNextHopNode(String destIPAddress) {
		if (destIPAddress == null) {
			return null;
		}
		BinaryTrieNode lastNode = root;
		int i = 0;
		BinaryTrieNode prevNode = null;
		for(i = 0; i < destIPAddress.length(); i++) {
			prevNode = lastNode;
			lastNode = lastNode.getChild(destIPAddress.charAt(i));
			if(lastNode == null) {
				return prevNode.firstHopNode;
			}
		}
		return lastNode.firstHopNode;
	}

	public boolean doesTerminate(String destIPAddress) {
		BinaryTrieNode lastNode = root;
		int i = 0;
		BinaryTrieNode prevNode = null;
		for(i = 0; i < destIPAddress.length(); i++) {
			prevNode = lastNode;
			lastNode = lastNode.getChild(destIPAddress.charAt(i));
			if(lastNode == null) {
				return prevNode.terminates;
			}
		}
		return lastNode.terminates;
	}

	public void compressTrie() {
		root.compress();
		return;
	}

	public int countTotalNodes() {
		IntWrap count = new IntWrap();
		count.count = 0;
		root.countTotal(count);
		return count.count;
	}

	public static void main(String[] args) {
		HashMap<String, nodeObject> hMap = new HashMap<String, nodeObject>();
		hMap.put("0011", new nodeObject("First Node",3));
		hMap.put("0010", new nodeObject("First Node",3));
		BinaryTrie test = new BinaryTrie(hMap);
		System.out.println(test.getNextHopNode("0011"));
		System.out.println(test.getNextHopNode("0010"));
		System.out.println(test.doesTerminate("001"));
		test.compressTrie();	
		System.out.println(test.getNextHopNode("0011"));
		System.out.println(test.getNextHopNode("0010"));
		System.out.println(test.doesTerminate("001"));

		/*Test 2 :: Divya's Test case*/

		HashMap<String, nodeObject> hMap2 = new HashMap<String, nodeObject>();
		hMap2.put("001", new nodeObject("R1",3));
		hMap2.put("010", new nodeObject("R2",1));
		hMap2.put("011", new nodeObject("R2",1));
		hMap2.put("100", new nodeObject("R4",2));
		hMap2.put("101", new nodeObject("R2",1));
		hMap2.put("110", new nodeObject("R2",1));
		hMap2.put("111", new nodeObject("R2",1));
		BinaryTrie test2 = new BinaryTrie(hMap2);
		System.out.println(test2.doesTerminate("01"));
		System.out.println(test2.countTotalNodes());
		test2.compressTrie();
		System.out.println("00:: " + test2.doesTerminate("00"));
		System.out.println("01:: " + test2.doesTerminate("01"));
		System.out.println("10:: " + test2.doesTerminate("10"));
		System.out.println("100:: " + test2.doesTerminate("100"));
		System.out.println("101:: " + test2.doesTerminate("101"));
		
		System.out.println("11:: " + test2.doesTerminate("11"));
		System.out.println(test2.countTotalNodes());
			
	
	}
}