package nl.tudelft.wdm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TPEStack {
    private final PatternNode p;
    // Deque is the java collections version of Stack
    private final Deque<Match> matches = new ArrayDeque<>();
    private final List<TPEStack> children = new ArrayList<>();
    /**
     * Parent stack. Called spar in the book
     */
    private TPEStack parentStack;

    public TPEStack(PatternNode p) {
        this.p = p;
    }

    public TPEStack(PatternNode p, TPEStack parent) {
        this(p);
        parentStack = parent;
        parent.addChild(this);
    }

    public PatternNode getPatternNode() {
        return p;
    }

    public TPEStack getParent() {
        return parentStack;
    }

    // gets the stacks for all descendants of p
    public void push(Match m) {
        matches.push(m);
    }

    public Match top() {
        return matches.peek();
    }

    public Match pop() {
        return matches.pop();
    }

    public void addChild(TPEStack child) {
        children.add(child);
    }
}
