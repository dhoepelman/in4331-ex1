package nl.tudelft.wdm.misc;


import org.xml.sax.Attributes;

import java.util.Iterator;

public class AttributesIterator implements Iterable<String>, Iterator<String> {
    final int length;
    private final Attributes attributes;
    int loc = 0;

    public AttributesIterator(Attributes attributes) {
        this.attributes = attributes;
        this.length = attributes.getLength();
    }

    @Override
    public boolean hasNext() {
        return loc < length;
    }

    @Override
    public String next() {
        return attributes.getValue(loc++);
    }

    @Override
    public Iterator<String> iterator() {
        return this;
    }
}
