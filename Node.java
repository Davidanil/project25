package php;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> _children = new ArrayList<Node>();
    private Node _parent = null;
    private String _kind = null;
    
    //Constructor
    public Node(Node parent) {
        set_parent(parent);
    }
    
    //Get Set
    public void setKind(String kind) {
    	_kind=kind;
    }
    
    public String getKind() {
    	return _kind;
    }
    
	public Node get_parent() {
		return _parent;
	}

	public void set_parent(Node parent) {
		_parent = parent;
	}

	//Other
    public List<Node> getChildren() {
        return _children;
    }

    public void addChild(Node child) {
        _children.add(child);
    }
    
}