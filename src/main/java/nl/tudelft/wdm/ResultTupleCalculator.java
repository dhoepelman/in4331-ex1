package nl.tudelft.wdm;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.*;

public class ResultTupleCalculator {
    private final Match m;
    private final boolean returnAll;
    private int currentWildCardNumber = 1;
    /**
     * Contains mappings from patternnodes to a unique string identifier
     */
    private BiMap<PatternNode, String> columnNamesMap = HashBiMap.create();

    public ResultTupleCalculator(Match m) {
        this(m, false);
    }

    /**
     * @param returnAll if true, will ignore the returnValue setting of PatternNodes and will return everything
     */
    public ResultTupleCalculator(Match m, boolean returnAll) {
        this.m = m;
        this.returnAll = returnAll;
    }

    /**
     * Returns the results as a table with an arbitrary integer as row number, PatternNode name as column, and preorder number as value.
     * If a node is optional it will not have a mapping (table.get() will return null)
     * Wildcards will be named "*1", "*2", ... ,"*n"
     */
    public ResultList calculate() {
        return calculate(m);
    }

    /**
     * Process the matches recursively
     * On a high-level, this works as follows by going through the match tree depth-first:
     * - Process a match by remembering it's (columnName,PreorderNumber)
     * - If the match has element children, process them
     * - Otherwise if the match is a leaf: Write a row to the table. The row contains the (columnName,PreOrderNumber) of the current match and all matches up the tree
     * - When a match is processed, forget the (columnName,PreOrderNumber) of that match
     */
    private ResultList calculate(Match currentMatch) {
        if (!currentMatch.hasChildren()) {
            // Leaf element
            return new ResultList().addElement(getColumnName(currentMatch), currentMatch.getPre());
        } else {
            // TODO;
            return null;
        }
    }

    /**
     * Get the (unique) column name for this matches PatternNode
     * If there are identically named columns it will append ' to the last added
     */
    private String getColumnName(Match m) {
        final PatternNode p = m.getStack().getPatternNode();
        String name = columnNamesMap.get(p);
        if (name == null) {
            name = p.getName();
            // Append ' until we get an unique name
            while (columnNamesMap.containsValue(name)) {
                name = name + "'";
            }
            columnNamesMap.put(p, name);
        }
        return name;
    }

    /**
     * Represents the result tuples as a list of maps
     */
    private static class ResultList {

        /**
         * The columns of the result table/tuples
         */
        final Set<String> columns = new HashSet<>();
        /**
         * The list of result tuples, which are represented as a map of columns to pre-order numbers
         */
        final List<Map<String, Integer>> results = new ArrayList<>();

        /**
         * True iff this resultslist is only a single tuple
         */
        private boolean isSingleTuple() {
            final int size = results.size();
            assert size != 0;
            return size == 1;
        }

        /**
         * Add an element pre-order number to all tuples
         */
        public ResultList addElement(String columnName, Integer preordernumber) {
            columns.add(columnName);
            // If there's no tuples yet this is the first one
            if (results.size() == 0) {
                results.add(new HashMap<>());
            }
            for (Map<String, Integer> tuple : results) {
                tuple.put(columnName, preordernumber);
            }
            return this;
        }

        /**
         * Take the union of two resultslists, i.e. combine the two result lists
         */
        public ResultList union(ResultList other) {
            columns.addAll(other.columns);
            results.addAll(other.results);
            return this;
        }

        /**
         * Merge two ResultLists, potentially altering them
         * Returns a merged ResultList
         */
        public ResultList merge(ResultList other) {
            // There are basically
            // Keep conflicting columns in here
            // A conflict exists if both ResultLists contain the same columns. This means a carthesian product of both lists will have to be taken
            List<String> conflicts = new ArrayList<>();
            List<String> nonconflicts = new ArrayList<>();
            // First determine conflicts
            for (String column : other.columns) {
                if (columns.contains(column)) {
                    conflicts.add(column);
                } else {
                    nonconflicts.add(column);
                }
            }
            // Merge the nonconflicting columns by copying
            for (Map<String, Integer> thisTuple : this.results) {
                for (Map<String, Integer> otherTuple : other.results) {
                    for (String column : nonconflicts) {

                    }
                }
            }
            return this;
        }
    }
}
