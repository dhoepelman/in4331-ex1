package nl.tudelft.wdm;

import nl.tudelft.wdm.misc.AttributesIterator;
import org.xml.sax.Attributes;
import org.xml.sax.DocumentHandler;

import java.util.Deque;

public class StackEval implements DocumentHandler {
    TreePattern q;

    /**
     * stack for the root of q
     */
    TPEStack rootStack;
    /**
     * pre number of the last element which has started:
     */
    int currentPre = 0;
    /**
     * pre numbers for all elements having started but not ended yet:
     */
    Deque<Integer> preOfOpenNodes;

    @Override
    public void startElement(String localName, Attributes attributes) {
        for (TPEStack s : rootStack.getDescendantStacks()) {
            if (localName == s.p.name && s.spar.top().getStatus() == Match.STATUS.OPEN) {
                Match m = new Match(currentPre, s.spar.top(), s);
                // create a match satisfying the ancestor conditions
                // of query node s.p
                s.push(m);
                preOfOpenNodes.push(currentPre);
            }
            currentPre++;
        }
        for (String attribute : new AttributesIterator(attributes)) {
            // similarly look for query nodes possibly matched
            // by the attributes of the currently started element
            for (TPEStack s : rootStack.getDescendantStacks()) {
                if (attribute.equals(s.getPatternNode().name) && s.getParent().top().getStatus() == Match.STATUS.OPEN) {
                    Match ma = new Match(currentPre, s.getParent().top(), s);
                    s.push(ma);
                }
            }
            currentPre++;
        }
    }

    @Override
    public void endElement(String localName) {
        // we need to find out if the element ending now corresponded
        // to matches in some stacks
        // first, get the pre number of the element that ends now:
        int preOflastOpen = preOfOpenNodes.pop();
        // now look for Match objects having this pre number:
        for (TPEStack s : rootStack.getDescendantStacks()) {
            if (s.getPatternNode().name == localName && s.top().getStatus() == Match.STATUS.OPEN && s.top().pre == preOfLastOpen) {
                // all descendants of this Match have been traversed by now.
                Match m = s.pop();
                // check if m has child matches for all children
                // of its pattern node
                for (pChild:
                     s.p.getChildren()) {
                    // pChild is a child of the query node for which m was created
                    if (m.children.get(pChild) == null) {
                        // m lacks a child Match for the pattern node pChild
                        // we remove m from its Stack, detach it from its parent etc.
                        remove(m, s);
                    }
                }
                m.close();
            }
        }
    }
}
