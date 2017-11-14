package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.ArrayList;
import java.util.List;

public class CallNode extends Node{
	private Node what;
	private List<Node> arguments = new ArrayList<Node>();;
	
	public CallNode(Node parent) {
		super(parent);
	}
	
	public Node getWhat(){
		return what;
	}
	
	public void setWhat(Node what){
		this.what = what;
	}
	
	public List<Node> getArguments() {
		return arguments;
	}

	public void setArguments(List<Node> arguments) {
		this.arguments = arguments;
	}

	@Override
	public List<Node> getChildren(){
		List<Node> list = new ArrayList<Node>();
		list.add(what);
		list.addAll(arguments);
		return list;
	}
	
	@Override
	public void addChild(Node node){
		arguments.add(node);
	}
}
