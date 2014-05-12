package nl.tudelft.wdm;

import java.util.Deque;
import java.util.List;

public class TPEStack {
    private PatternNode p;
    // Deque is the java collections version of Stack
    private Deque<Match> matches;
    private TPEStack spar;

    /**
     * Gets the stacks for all descendants of p
     */
    public List<TPEStack> getDescendantStacks() {
        //TODO: Implement
    }

    public PatternNode getPatternNode() {
        return p;
    }

    public TPEStack getParent() {
        return spar;
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
}
