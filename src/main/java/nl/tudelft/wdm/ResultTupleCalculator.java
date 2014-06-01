package nl.tudelft.wdm;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.*;

public class ResultTupleCalculator {
    private final Match rootMatch;

    /**
     * Contains mappings from patternnodes to a unique string identifier
     */
    private BiMap<PatternNode, String> columnNamesMap = HashBiMap.create();

    public BiMap<PatternNode, String> getColumnNamesMap() {
        return columnNamesMap;
    }

    public ResultTupleCalculator(Match rootMatch) {
        this.rootMatch = rootMatch;
    }


    public ResultList calculate() {
        return calculate(rootMatch);
    }

    private ResultList calculate(Match currentMatch) {
        if (!currentMatch.hasChildren()) {
            // Leaf element
            return new ResultList().addElement(getColumnName(currentMatch), currentMatch.getPre());
        } else {
            List<ResultList> currentMatchChildResults = new ArrayList<>();
            for(PatternNode childP : currentMatch.getChildren().keySet()) {
                Collection<Match> childMatches = currentMatch.getChildren().get(childP);
                ResultList childResults = new ResultList();
                // Take the union of all the child matches for this pattern node
                for(Match childM : childMatches) {
                    childResults = childResults.union(calculate(childM));
                }
                currentMatchChildResults.add(childResults);
            }
            // Flatten the match child results by taking the carthesian product
            ResultList currentMResults = currentMatchChildResults.get(0);
            for(int i=1; i < currentMatchChildResults.size(); i++) {
                currentMResults = currentMResults.merge(currentMatchChildResults.get(i));
            }
            currentMResults = currentMResults.addElement(getColumnName(currentMatch), currentMatch.getPre());
            return currentMResults;
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
    public static class ResultList {

        /**
         * The columns of the result table/tuples
         */
        final Set<String> columns = new HashSet<>();
        /**
         * The list of result tuples, which are represented as a map of columns to pre-order numbers
         */
        final List<Map<String, Integer>> results = new ArrayList<>();

        public Set<String> getColumns() {
            return columns;
        }

        public List<Map<String, Integer>> getResults() {
            return results;
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
         * Merge two ResultLists by taking their cartesian product
         */
        public ResultList merge(ResultList that) {
            ResultList product = new ResultList();
            // Merge the two column sets
            product.columns.addAll(this.columns);
            product.columns.addAll(that.columns);
            // Check if there are any conflicts, if so we'll need make every tuple into 2
            boolean conflicts = false;
            for(String column : that.columns) {
                if(this.columns.contains(column)) {
                    conflicts = true;
                    break;
                }
            }
            // Now for every this row, combine it with the other rows
            for(Map<String, Integer> thisTuple : this.results) {
                for(Map<String, Integer> thatTuple : that.results) {
                    Map<String, Integer> newTuple = new HashMap<>(thisTuple);
                    newTuple.putAll(thatTuple);
                    product.results.add(newTuple);
                    // If we have conflicts, now make the other tuple
                    if(conflicts) {
                        newTuple = new HashMap<>(thatTuple);
                        newTuple.putAll(thisTuple);
                        product.results.add(newTuple);
                    }
                }
            }
            return product;
        }
    }
}
