package nl.tudelft.wdm;

import nl.tudelft.wdm.misc.AttributesIterator;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayDeque;
import java.util.Deque;

public class StackEval extends DefaultHandler {
    final PatternNode root;
    /**
     * stack for the root of root
     */
    TPEStack rootStack;
    /**
     * pre number of the last element which has started:
     */
    int currentPre = 0;
    /**
     * pre numbers for all elements having started but not ended yet:
     */
    Deque<Integer> openNodesPreNumbers = new ArrayDeque<>();
    private Match rootMatch;
    private PatternNode current;

    public StackEval(PatternNode root) {
        this.root = root;
        this.rootStack = root.getStack();
    }

    public Match getRootMatch() {
        return rootMatch;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentPre++;
        if (qName.equals(root.getName())) {
            addMatch(qName, root);
        } else {
            //for (TPEStack stack : rootStack.getDescendantStacks()) {
            for (PatternNode node : current.getChildren()) {
                addMatch(qName, node);
            }
        }
        for (String attribute : new AttributesIterator(attributes)) {
            currentPre++;
            // similarly look for query nodes possibly matched
            //for (TPEStack s : rootStack.getDescendantStacks()) {
            for (PatternNode node : current.getChildren()) {
                TPEStack s = node.getStack();
                if (attribute.equals(node.getName()) && s.getParent().top().getStatus() == Match.STATUS.OPEN) {
                    Match ma = new Match(currentPre, s.getParent().top(), s);
                    s.push(ma);
                }
            }
        }
    }

    private void addMatch(String qName, PatternNode node) {
        TPEStack stack = node.getStack();
        if (qName.equals(node.getName()) && (node.isRoot() || stack.getParent().top().getStatus() == Match.STATUS.OPEN)) {
            current = node;
            Match m = new Match(currentPre, (node.isRoot() ? null : stack.getParent().top()), stack);
            if (node.isRoot()) {
                rootMatch = m;
            }
            // create a match satisfying the ancestor conditions
            // of query node stack.p
            stack.push(m);
            openNodesPreNumbers.push(currentPre);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        // we need to find out if the element ending now corresponded
        // to matches in some stacks
        // first, get the pre number of the element that ends now:
        if (openNodesPreNumbers.isEmpty()) {
            // We have no open elements, ignore this element
            return;
        }
        // now look for Match objects having this pre number:
        //for (TPEStack stack : rootStack.getDescendantStacks()) {
        //    if (stack.getPatternNode().getName().equals(qName) && stack.top().getStatus() == Match.STATUS.OPEN && stack.top().getPre() == preOfLastOpen) {
        if (current.getName().equals(qName)) {
            int preOfLastOpen = openNodesPreNumbers.pop();
            TPEStack stack = current.getStack();
            // all descendants of this Match have been traversed by now.
            Match m = stack.pop();
            // check if m has child matches for all children
            // of its pattern node
            for (PatternNode pChild : current.getChildren()) {
                // pChild is a child of the query node for which m was created
                if (m.getChildren().get(pChild) == null) {
                    // m lacks a child Match for the pattern node pChild
                    // we remove m from its Stack, detach it from its parent etc.
                    m.getParent().removeChild(pChild, m);
                }
            }
            m.close();
            current = current.getParent();
        }
        //}
    }

    // Methoden van de SAX2 interface onderstaand, misschien implementeren indien nodig?

    @Override
    public void setDocumentLocator(Locator locator) {
        // TODO: Implement
    }

    @Override
    public void startDocument() throws SAXException {
        // TODO: Implement
    }

    @Override
    public void endDocument() throws SAXException {
        // TODO: Implement
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        // TODO: Implement
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        // TODO: Implement
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // TODO: Implement
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        // TODO: Implement
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        // TODO: Implement
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        // TODO: Implement
    }
}
