package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.ArrayList;
import java.util.List;

public class IdentifierNode extends Node{
	private String name;
	
	public IdentifierNode(Node parent) {
		super(parent);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public List<Node> getChildren(){
		return new ArrayList<Node>();
	}
	
	@Override
	public void addChild(Node node){
	}
}
