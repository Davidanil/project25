package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.ArrayList;
import java.util.List;

public class OffsetLookupNode extends Node{
	private Node what;
	private Node offset;

	public OffsetLookupNode(Node parent) {
		super(parent);
	}

	public Node getWhat() {
		return what;
	}

	public void setWhat(Node what) {
		this.what = what;
	}

	public Node getOffset() {
		return offset;
	}

	public void setOffset(Node offset) {
		this.offset = offset;
	}
	
	@Override
	public List<Node> getChildren(){
		List<Node> list = new ArrayList<Node>();
		list.add(what);
		list.add(offset);
		return list;
	}
	
	@Override
	public void addChild(Node node){
	}
}
