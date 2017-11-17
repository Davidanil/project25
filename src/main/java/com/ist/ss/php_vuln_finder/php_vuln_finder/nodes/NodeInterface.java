package com.ist.ss.php_vuln_finder.php_vuln_finder.nodes;

import java.util.List;

public interface NodeInterface {
	public List<Node> getChildren();
	public void addChild(Node node);
}
