package nl.tudelft.wdm;


import java.util.ArrayList;
import java.util.List;

public class PatternNode {
    private final String name;
    private final List<PatternNode> children = new ArrayList<>();
    private final PatternNode parent;
    private final TPEStack stack;
    private final boolean optional;
    private final boolean attribute;
    private final boolean returnResult;
    private final String valuePredicate;

    private PatternNode(String name, PatternNode parent, boolean optional, boolean attribute, boolean returnResult, String valuePredicate) {
        this.name = name;
        this.stack = new TPEStack(this, (parent == null ? null : parent.getStack()));
        this.optional = optional;
        this.attribute = attribute;
        this.parent = parent;
        this.returnResult = returnResult;
        this.valuePredicate = valuePredicate;
        if (parent != null) {
            parent.addChild(this);
        }
    }

    public boolean isAttribute() {
        return attribute;
    }

    public boolean isOptional() {
        return optional;
    }

    public boolean isReturnResult() {
        return returnResult;
    }

    public String getValuePredicate() {
        return valuePredicate;
    }

    public boolean hasValuePredicate() {
        return valuePredicate != null;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatternNode that = (PatternNode) o;

        return name.equals(that.name) && !(parent != null ? !parent.equals(that.parent) : that.parent != null);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public void addChild(PatternNode p) {
        children.add(p);
    }

    public List<PatternNode> getChildren() {
        return children;
    }

    public TPEStack getStack() {
        return stack;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public PatternNode getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "PatternNode{" + name + "," + (parent == null ? "root" : parent.getName()) + '}';
    }

    public static class Builder {
        private String name;
        private PatternNode parent;
        private boolean optional = false;
        private boolean attribute = false;
        private boolean returnResult = false;
        private String valuePredicate = null;

        /**
         * Create a builder for a root wildcard node
         */
        public Builder() {
            makeWildcardNode();
        }

        ;

        /**
         * Create a builder for a root node
         */
        public Builder(String name) {
            setName(name);
        }

        /**
         * Create a builder for a wildcard node
         */
        public Builder(PatternNode parent) {
            setParent(parent);
            makeWildcardNode();
        }

        /**
         * Create a builder for a normal node
         */
        public Builder(String name, PatternNode parent) {
            setName(name);
            setParent(parent);
        }

        public Builder makeWildcardNode() {
            this.name = "*";
            return this;
        }

        public Builder setParent(PatternNode parent) {
            this.parent = parent;
            return this;
        }

        public Builder makeOptional() {
            this.optional = true;
            return this;
        }

        public Builder makeAttributeNode() {
            this.attribute = true;
            return this;
        }

        public Builder makeReturnResult() {
            this.returnResult = true;
            return this;
        }

        public Builder setValuePredicate(String valuePredicate) {
            this.valuePredicate = valuePredicate;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public PatternNode build() {
            if (name == null) {
                throw new RuntimeException("Name for PatternNode not provided");
            }
            return new PatternNode(name, parent, optional, attribute, returnResult, valuePredicate);
        }
    }
}
