package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.ArrayList;
import java.util.List;

public class ProgramNode extends Node{
	private List<Node> children = new ArrayList<Node>();
	private List<String> errors = new ArrayList<String>();
	
	public ProgramNode() {
		super(null);
	}
	
	public void setChildren(List<Node> children){
		this.children = children;
	}
	
	@Override
	public List<Node> getChildren(){
		return this.children;
	}
	
	public void setErrors(List<String> errors){
		this.errors = errors;
	}
	
	public List<String> getErrors(){
		return this.errors;
	}
	
	@Override
	public void addChild(Node node){
		children.add(node);
	}
}
