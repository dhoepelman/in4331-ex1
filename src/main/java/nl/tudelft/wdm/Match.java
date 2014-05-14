package nl.tudelft.wdm;

import com.google.common.collect.Multimap;

public class Match {
    // Original Map <PatternNode, Array<Match>> children; , but Array isn't a class in Java...
    // I guess Map <PatternNode, List<Match>> was meant, and Guava has a MultiMap that is abstracts Map<P, List<Q>>
    private Multimap<PatternNode, Match> children;
    private TPEStack st;
    private int start;
    private STATUS status;
    private Match parent;

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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
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
