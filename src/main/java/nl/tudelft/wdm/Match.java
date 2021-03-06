package nl.tudelft.wdm;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Match {
    // Original Map <PatternNode, Array<Match>> children; , but Array isn't a class in Java...
    // I guess Map <PatternNode, List<Match>> was meant, and Guava has a MultiMap that is abstracts Map<P, List<Q>>
    private final Multimap<PatternNode, Match> children = HashMultimap.create();
    private final TPEStack st;
    private final int pre;
    private final Match parent;
    private final String name;
    private String textValue;
    private STATUS status = STATUS.OPEN;

    public Match(int pre, Match parent, TPEStack stack, String name) {
        this.pre = pre;
        this.parent = parent;
        this.st = stack;
        if (parent != null) {
            parent.addChild(stack.getPatternNode(), this);
        }
        this.name = name;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public String getName() {
        return name;
    }

    public STATUS getStatus() {
        return status;
    }

    public void close() {
        this.status = STATUS.CLOSED;
    }

    public Multimap<PatternNode, Match> getChildren() {
        return children;
    }

    public boolean hasChildren() {
        return getChildren().size() > 0;
    }

    public void addChild(PatternNode pn, Match m) {
        children.put(pn, m);
    }

    public TPEStack getStack() {
        return st;
    }


    public int getPre() {
        return pre;
    }

    public Match getParent() {
        return parent;
    }

    public void removeChild(Match m) {
        children.remove(m.getStack().getPatternNode(), m);
    }

    public String toString() {
        return toString(0);
    }

    private String toString(int tab) {
        StringBuilder tabsSB = new StringBuilder();
        for (int i = 0; i < tab; i++) {
            tabsSB.append("\t");
        }
        String tabs = tabsSB.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%sMatch(%s, %s, %d) {", tabs, getName(), getStatus().name(), getPre()));
        if (children.size() > 0) {
            sb.append("\n");
            for (Match m : children.values()) {
                sb.append(m.toString(tab + 1));
                sb.append(",\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public enum STATUS {
        OPEN,
        CLOSED
    }
}
