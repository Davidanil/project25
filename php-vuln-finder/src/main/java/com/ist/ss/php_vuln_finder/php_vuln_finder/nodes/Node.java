package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

public abstract class Node implements NodeInterface {
	private String kind;
	private Node parent;
	private int level; //TODO:  add level to the node constructor, might be useful

	public Node(Node parent) {
		this.parent = parent;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getKind() {
		return this.kind;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getParent() {
		return this.parent;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}

	@Override
	public String toString() {
		return getKind() + getLevel();

	}

	// AUX FUNCTIONS

	// Search tree for specific strings
	
	/**
	 * @param childKind - kind of child we are searching for
	 * @return Node of first matching child
	 */
	public Node getDirectChildByKind(String childKind) {
		for (Node n : this.getChildren())
			if (n.getKind().equals(childKind)) 
				return n;
			
		return null;
	}
}
