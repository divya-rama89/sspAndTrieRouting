import java.util.*;


public class BinaryTrieNode {
	private BinaryTrieNode zero;
	private BinaryTrieNode one;

	public boolean terminates = false;
	public Integer weight;
	public String firstHopNode;

	private char character;

	public BinaryTrieNode() {
		zero = null;
		one = null;
	}

	public BinaryTrieNode(char character) {
		this.character = character;
	}

	public char getChar() {
		return character;
	}

	public void countTotal(IntWrap count) {
		count.count += 1;
		if(this.zero != null) {
			this.zero.countTotal(count);
		}
		if(this.one != null) {
			this.one.countTotal(count);
		}
	}
	
	public void addNextHopNode(String destination, String Node) {
		if(destination == null || destination.isEmpty()) {
			return;
		}
		
		BinaryTrieNode child;
		char firstChar = destination.charAt(0);
		
		BinaryTrieNode t = getChild(firstChar);

		if(t == null) {
			child = new BinaryTrieNode(firstChar);
			if(firstChar == '0') {
				zero = child;
			} else {
				one = child;
			}
		} else {
			child = t;
		}
		
		if(destination.length() > 1) {
			child.addNextHopNode(destination.substring(1), Node);
		} else {
			child.terminates = true;
			child.firstHopNode = Node;
		}
	}

	BinaryTrieNode getChild(char c) {
		if(c == '0') {
			return zero;
		}
		else if(c == '1') {
			return one;
		}
		else {
			return null;
		}

	}

	public void compress() {
		if(this.zero != null) {
			this.zero.compress();
		}

		if(this.one != null) {
			this.one.compress();
		}

		if(this.zero != null && this.one != null) {
			if(this.zero.firstHopNode != null && this.one.firstHopNode != null) {
				if(this.zero.firstHopNode.equals(this.one.firstHopNode)) {
					this.firstHopNode = this.zero.firstHopNode;
					this.zero = null;
					this.one = null;
					this.terminates = true;
				}
			}
		} else if(this.zero != null || this.one != null) {
			String temp = "";
			if(this.zero != null) {
				temp = this.zero.firstHopNode;
			} else if(this.one != null) {
				temp = this.one.firstHopNode;
			}
			this.firstHopNode = temp;
			this.zero = null;
			this.one = null;
			this.terminates = true;
		}
	}
	
}