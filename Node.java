import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> _children = new ArrayList<Node>();
    private Node _parent = null;
    private String _kind = "root";
    private int _lvl = 0;
    private String _text = "";
    	
    //Constructor
    public Node(Node parent) {
        set_parent(parent);
        setLvl();
    }
    
    //Get Set
    public void setLvl() {
    	if (_parent != null)
    		_lvl = _parent.getLvl() + 1;
    }
    
    private int getLvl() {
		return _lvl;
	}

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
    
    public void addText(String text) {
    	_text += text;
    }
    
    @Override
    public String toString() {
    	return getKind() + getLvl();

    }
    public void printTree() {
    	if (_parent != null)
    		System.out.println("Parent: " + _parent.getKind());
    	System.out.println("Level: " + getLvl() + "\nNode: " + getKind());
    	System.out.println(_text.contains("_GET"));
    	System.out.println();
    	for (Node item : _children) {
    	    item.printTree();
    	}
    }
}