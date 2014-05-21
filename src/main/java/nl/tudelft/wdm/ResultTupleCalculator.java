package nl.tudelft.wdm;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

import java.util.HashMap;
import java.util.Map;

public class ResultTupleCalculator {
    private final Match m;
    private final Table<Integer, String, Integer> results = HashBasedTable.create();
    private final Map<String, Integer> currentMappings = new HashMap<>();
    /**
     * Contains the attributes for all unprocessed matches
     */
    private final Table<Match, String, Integer> currentAttributeMappings = HashBasedTable.create();
    private final boolean returnAll;
    private Integer currentRow = 0;
    private int currentWildCardNumber = 1;
    private Map<PatternNode, String> wildcardMap = new HashMap<>();

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
    public Table<Integer, String, Integer> calculate() {
        calculate(m);
        return Tables.unmodifiableTable(results);
    }

    /**
     * Process the matches recursively
     * On a high-level, this works as follows by going through the match tree depth-first:
     * - Process a match by remembering it's (columnName,PreorderNumber)
     * - If the match has element children, process them
     * - Otherwise if the match is a leaf: Write a row to the table. The row contains the (columnName,PreOrderNumber) of the current match and all matches up the tree
     * - When a match is processed, forget the (columnName,PreOrderNumber) of that match
     */
    private void calculate(Match currentMatch) {
        final String columnName = getColumnName(currentMatch);
        if (returnAll || currentMatch.getStack().getPatternNode().isReturnResult()) {
            // Put the current match into the mapping
            currentMappings.put(columnName, currentMatch.getPre());
        }

        // Process all the attribute children
        for (Match child : currentMatch.getChildren().values()) {
            final PatternNode childPatternNode = child.getStack().getPatternNode();
            if (childPatternNode.isAttribute()) {
                assert child.getChildren().size() == 0;
                if (returnAll || childPatternNode.isReturnResult()) {
                    currentAttributeMappings.put(currentMatch, getColumnName(child), child.getPre());
                }
            }
        }

        boolean hasElementChildren = false;
        // Process all the element children
        // This cannot be done in the same pass as the attributes, as all attributes need to have been processed before any children are processed (otherwise the tuples might miss attributes)
        for (Match child : currentMatch.getChildren().values()) {
            if (!child.getStack().getPatternNode().isAttribute()) {
                // Continue down the tree
                hasElementChildren = true;
                calculate(child);
            }
        }

        if (!hasElementChildren) {
            // We are at a leaf, make the table row
            makeTableRow();
        }

        // We're done with this match, remove it from the mappings
        currentMappings.remove(columnName);
        currentAttributeMappings.row(currentMatch).clear();
    }

    /**
     * Make a table row out of the current mappings
     */
    private void makeTableRow() {
        for (Map.Entry<String, Integer> entry : currentMappings.entrySet()) {
            results.put(currentRow, entry.getKey(), entry.getValue());
        }
        // Now add the attributes of all the matches
        for (Map<String, Integer> attributes : currentAttributeMappings.rowMap().values()) {
            for (Map.Entry<String, Integer> attribute : attributes.entrySet()) {
                results.put(currentRow, attribute.getKey(), attribute.getValue());
            }
        }
        currentRow++;
    }

    /**
     * Get the column name for this match
     * Trivial if the PatternNode is not a wildcard, but provides a unique String id for each different wildcard
     */
    private String getColumnName(Match m) {
        final PatternNode p = m.getStack().getPatternNode();
        if (!p.isWildCard()) {
            return p.getName();
        } else {
            // Get an unique mapping for each wildcard
            String name = wildcardMap.get(p);
            if (name == null) {
                name = String.format("*%d", currentWildCardNumber);
                currentWildCardNumber++;
                wildcardMap.put(p, name);
            }
            return name;
        }
    }
}
