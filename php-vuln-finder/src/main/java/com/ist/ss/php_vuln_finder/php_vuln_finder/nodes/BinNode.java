package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.ArrayList;
import java.util.List;

public class BinNode extends Node{
	private String type;
	private Node left;
	private Node right;
	
	public BinNode(Node parent) {
		super(parent);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
