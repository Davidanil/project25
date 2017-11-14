package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.ArrayList;
import java.util.List;

public class VariableNode extends Node{
	private String name;
	
	public VariableNode(Node parent) {
		super(parent);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	@Override
	public List<Node> getChildren(){
		return new ArrayList<Node>();
	}
	
	@Override
	public void addChild(Node node){
	}
	
}
