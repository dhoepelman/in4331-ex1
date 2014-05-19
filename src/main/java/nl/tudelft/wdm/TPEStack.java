package nl.tudelft.wdm;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TPEStack {
    private PatternNode p;
    // Deque is the java collections version of Stack
    private Deque<Match> matches;
    /**
     * Parent stack. Called spar in the book
     */
    private TPEStack parentStack;

    private List<TPEStack> children;

    public TPEStack() {
        children = new ArrayList<>();
    }

    public TPEStack(TPEStack parent) {
        this();
        parentStack = parent;
        parent.addChild(this);
    }

    /**
     * Gets the stacks for all descendants of p
     */
    public List<TPEStack> getDescendantStacks() {
        List<TPEStack> descStacks = new ArrayList<>();
        for (TPEStack child : children) {
            descStacks.add(child);
            descStacks.addAll(child.getDescendantStacks());
        }

        return descStacks;
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
