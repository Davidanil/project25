package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

public abstract class Node implements NodeInterface {
	private String kind;
	private Node parent;
	private int level;

	public Node(Node parent) {
		this.parent = parent;
		if(parent != null)
			this.level = parent.getLevel() + 1;
		else
			this.level = 0; //program node use case
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

}
