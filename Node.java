import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Node> _children = new ArrayList<Node>();
    private Node _parent = null;
    private String _kind = "root";
    private int _lvl = 0;
    private String _text = "";
    private String _name = "";
    private boolean _vuln = false;
    	
    //Constructor
    public Node(Node parent) {
        set_parent(parent);
        setLvl();
    }
    
    //Get Set
   
    public String getName() {
    	return _name;
    }
    
    public void setName(String name) {
    	_name = name;
    }
    
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
    
    //Find name of parameter
    public String findName(String text) {
    	String array[] = text.split("\"");
    	for(int i=0; i<array.length ;i++) {
    		if(array[i].contains("name")) {
    			return array[i+2];
    			}
    		}
		return "";
    }
    
    //Search tree for specific strings
    public String parentSearch(Node start, String parent, String child, boolean kind) {
    	if (start.getKind().contains(parent))
			for(Node n : start.getChildren())
				if(n.getKind().contains(child)) {
					n.setName(n.findName(n.getText()));
					if(kind)
						return n.getKind() + "\n" + n.getName();
					return n.getName();
					}
    	return "";
    }
    
    public String searchTree(Node start) {
    	String ret = "";
    	while (start.get_parent() != null) {
    		start = start.get_parent();
    		ret = parentSearch(start, "assi", "var", true);
    		if(!ret.isEmpty())
    			return ret;
    		ret = parentSearch(start, "cal", "ident", true);
    		if(!ret.isEmpty())
    			return ret;
    	}
    	return "Not found";
    }
    
    //Print Tree
    public void printTree() {
    	addVuln(getText().contains(": \"q\""));
    	addVuln(getText().contains(": \"u\""));
    	addVuln(getText().contains("_GET"));
    	
    	if (get_parent()!= null)
    		System.out.println("Parent: " + get_parent().getKind());
    	System.out.println("Level: " + getLvl());
    	System.out.println("Node: " + getKind());
    	System.out.println("Vuln: " + getVuln());
    	
    	if (getVuln())
    		System.out.println(searchTree(this));
    	System.out.println();
    	for (Node item : getChildren()) {
    	    item.printTree();
    	}
    	System.out.println();
    }
    
    public String printTree2() {
    	addVuln(getText().contains("_query"));
    	if (getVuln()) {
    		if(get_parent() != null)
    		System.out.println(parentSearch(get_parent(), "cal", "var", false));
    		return parentSearch(get_parent(), "cal", "var", false);
    	}
    	for (Node item : getChildren()) {
    	    item.printTree2();
    	}
    	return "";
    }
}