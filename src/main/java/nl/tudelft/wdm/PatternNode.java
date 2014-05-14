package nl.tudelft.wdm;


import java.util.ArrayList;
import java.util.List;

public class PatternNode {
    private final String name;
    private final List<PatternNode> children = new ArrayList<>();

    public PatternNode(String name) {
        this.name = name;
    }

    public PatternNode(String name, PatternNode parent) {
        this(name);
        parent.addChild(this);
    }

    public String getName() {
        return name;
    }

    public void addChild(PatternNode p) {
        children.add(p);
    }
}
