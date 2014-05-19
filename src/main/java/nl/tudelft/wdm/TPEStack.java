package nl.tudelft.wdm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TPEStack {
    private final PatternNode p;
    // Deque is the java collections version of Stack
    private Deque<Match> matches = new ArrayDeque<>();
    /**
     * Parent stack. Called spar in the book
     */
    private TPEStack parentStack;

    private List<TPEStack> children = new ArrayList<>();

    public TPEStack(PatternNode p) {
        this.p = p;
    }

    public TPEStack(PatternNode p, TPEStack parent) {
        this(p);
        parentStack = parent;
        parent.addChild(this);
    }

    /**
     * Gets this and all descendant stacks
     */
    public List<TPEStack> getDescendantStacks() {
        List<TPEStack> descStacks = new ArrayList<>();
        descStacks.add(this);
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

    public void remove(Match m) {
        matches.remove(m);
    }

    public void addChild(TPEStack child) {
        children.add(child);
    }
}
