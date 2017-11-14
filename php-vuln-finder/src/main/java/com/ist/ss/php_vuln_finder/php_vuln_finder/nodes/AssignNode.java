package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.ArrayList;
import java.util.List;

public class AssignNode extends Node{
	private String operator;
	private Node left;
	private Node right;
	
	public AssignNode(Node parent) {
		super(parent);
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Node getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}
	
	@Override
	public List<Node> getChildren(){
		List<Node> list = new ArrayList<Node>();
		list.add(left);
		list.add(right);
		return list;
	}
	
	@Override
	public void addChild(Node node){
	}
}
