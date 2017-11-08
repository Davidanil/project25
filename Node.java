import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> _children = new ArrayList<Node>();
    private Node _parent = null;
    private String _kind = "root";
    private int _lvl = 0;
    private String _text = "";
    private boolean _vuln=false;
    	
    //Constructor
    public Node(Node parent) {
        set_parent(parent);
        setLvl();
    }
    
    //Get Set
    
    public boolean getVuln() {
    	return _vuln;
    }
    
    public void setVuln(boolean vuln) {
    	_vuln = vuln;
    }
    
    public void addVuln(boolean vuln) {
    	if (vuln==true) _vuln=vuln;
    }
    
    public String getText() {
    	return _text;
    }
    
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
    
    public String parentSearch(Node start, String parent, String child) {
    	if (start.getKind().contains(parent))
			for(Node n : start.getChildren())
				if(n.getKind().contains(child))
					return n.getKind() + "\n" + n.getText();
    	return "";
    }
    
    public String searchTree(Node start) {
    	String ret = "";
    	while (start.get_parent() != null) {
    		start = start.get_parent();
    		ret = parentSearch(start, "assi", "var");
    		if(!ret.isEmpty())
    			return ret;
    		ret = parentSearch(start, "cal", "ident");
    		if(!ret.isEmpty())
    			return ret;
    	}
    	return "Not found";
    }
    
    public void printTree() {
    	addVuln(getText().contains(": \"q\""));
    	addVuln(getText().contains(": \"u\""));
    	addVuln(getText().contains("_GET"));
    	if (get_parent()!= null)
    		System.out.println("Parent: " + get_parent().getKind());
    	System.out.println("Level: " + getLvl() + "\nNode: " + getKind() + getText());
    	System.out.println(getVuln());
    	if (getVuln())
    		System.out.println(searchTree(this));
    	System.out.println();
    	for (Node item : getChildren()) {
    	    item.printTree();
    	}
    	System.out.println();
    }
}