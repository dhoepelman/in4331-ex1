package nl.tudelft.wdm;


import java.util.ArrayList;
import java.util.List;

public class PatternNode {
    private final String name;
    private final List<PatternNode> children = new ArrayList<>();
    private final PatternNode parent;
    private final TPEStack stack;

    public PatternNode(String name) {
        this.name = name;
        this.stack = new TPEStack(this);
        this.parent = null;
    }

    public PatternNode(String name, PatternNode parent) {
        this.name = name;
        this.stack = new TPEStack(this, parent.getStack());
        this.parent = parent;
        parent.addChild(this);
    }

    public String getName() {
        return name;
    }

    public void addChild(PatternNode p) {
        children.add(p);
    }

    public List<PatternNode> getChildren() {
        return children;
    }

    public TPEStack getStack() {
        return stack;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public PatternNode getParent() {
        return parent;
    }
}
