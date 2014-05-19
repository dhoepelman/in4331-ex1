package nl.tudelft.wdm;


import java.util.ArrayList;
import java.util.List;

public class PatternNode {
    private final String name;
    private final List<PatternNode> children = new ArrayList<>();
    private final TPEStack stack;

    private PatternNode(String name, TPEStack stack) {
        this.name = name;
        this.stack = stack;
    }

    public PatternNode(String name) {
        this(name, new TPEStack());
    }

    public PatternNode(String name, PatternNode parent) {
        this(name, new TPEStack(parent.getStack()));
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
}
