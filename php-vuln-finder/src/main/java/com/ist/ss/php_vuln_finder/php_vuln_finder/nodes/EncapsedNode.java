package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.ArrayList;
import java.util.List;

public class EncapsedNode extends Node{
	private String type;
	private List<Node> value = new ArrayList<Node>();
	
	public EncapsedNode(Node parent) {
		super(parent);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Node> getValue() {
		return value;
	}
	public void setValue(List<Node> value) {
		this.value = value;
	}

	@Override
	public List<Node> getChildren(){
		return value;
	}
	
	@Override
	public void addChild(Node node){
		value.add(node);
	}
}
