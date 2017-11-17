package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.ArrayList;
import java.util.List;

public class EchoNode extends Node{
	private List<Node> arguments = new ArrayList<Node>();
	
	public EchoNode(Node parent) {
		super(parent);
	}
	
	public void setArguments(List<Node> arguments){
		this.arguments = arguments;
	}
	
	public List<Node> getArguments(){
		return this.arguments;
	}
	
	@Override
	public List<Node> getChildren(){
		return this.arguments;
	}
	
	@Override
	public void addChild(Node node){
		arguments.add(node);
	}
	
}
