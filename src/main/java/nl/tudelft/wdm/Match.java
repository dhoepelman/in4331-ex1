package nl.tudelft.wdm;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Match {
    // Original Map <PatternNode, Array<Match>> children; , but Array isn't a class in Java...
    // I guess Map <PatternNode, List<Match>> was meant, and Guava has a MultiMap that is abstracts Map<P, List<Q>>
    private final Multimap<PatternNode, Match> children = HashMultimap.create();
    private TPEStack st;
    private int pre;
    private STATUS status = STATUS.OPEN;
    private Match parent;

    public Match(int pre, Match parent, TPEStack stack) {
        this.pre = pre;
        this.parent = parent;
        this.st = stack;
        parent.addChild(stack.getParent().getPatternNode(), this);
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

    public void addChild(PatternNode pn, Match m) {
        children.put(pn, m);
    }

    public TPEStack getSt() {
        return st;
    }

    public void setSt(TPEStack st) {
        this.st = st;
    }

    public int getPre() {
        return pre;
    }

    public void setPre(int pre) {
        this.pre = pre;
    }

    public Match getParent() {
        return parent;
    }

    public void setParent(Match parent) {
        this.parent = parent;
    }

    public enum STATUS {
        OPEN,
        CLOSED
    }
}
