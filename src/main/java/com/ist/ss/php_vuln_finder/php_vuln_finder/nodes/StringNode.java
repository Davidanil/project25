package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.ArrayList;
import java.util.List;

public class StringNode extends Node{
	private String value;
	
	public StringNode(Node parent, String value) {
		super(parent);
		this.setValue(value);
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
	
	@Override
	public List<Node> getChildren(){
		return new ArrayList<Node>();
	}
	
	@Override
	public void addChild(Node node){
	}
}
