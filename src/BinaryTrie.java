import java.util.*;

public class BinaryTrie {

	private BinaryTrieNode root;

	public BinaryTrie(HashMap<String, String> hMap) {
		root = new BinaryTrieNode();
		for(String key : hMap.keySet()) {
			root.addNextHopNode(key, hMap.get(key));
		}
	}

	public String getNextHopNode(String destIPAddress) {
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
		HashMap<String, String> hMap = new HashMap<String, String>();
		hMap.put("0011", "First Node");
		hMap.put("0010", "First Node");
		BinaryTrie test = new BinaryTrie(hMap);
		System.out.println(test.getNextHopNode("0011"));
		System.out.println(test.getNextHopNode("0010"));
		System.out.println(test.doesTerminate("001"));
		test.compressTrie();	
		System.out.println(test.getNextHopNode("0011"));
		System.out.println(test.getNextHopNode("0010"));
		System.out.println(test.doesTerminate("001"));

		/*Test 2 :: Divya's Test case*/

		HashMap<String, String> hMap2 = new HashMap<String, String>();
		hMap2.put("001", "R1");
		hMap2.put("010", "R2");
		hMap2.put("011", "R2");
		hMap2.put("100", "R4");
		hMap2.put("101", "R2");
		hMap2.put("110", "R2");
		hMap2.put("111", "R2");
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