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
    private Integer currentRow = 0;
    private int currentWildCardNumber = 1;
    private Map<PatternNode, String> wildcardMap = new HashMap<>();

    public ResultTupleCalculator(Match m) {
        this.m = m;
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

    private void calculate(Match currentMatch) {
        final String columnName = getColumnName(currentMatch);
        // Put the current match into the mapping
        currentMappings.put(columnName, currentMatch.getPre());

        if (currentMatch.getChildren().size() == 0) {
            // We are at a leaf, make the table row
            for (Map.Entry<String, Integer> entry : currentMappings.entrySet()) {
                results.put(currentRow, entry.getKey(), entry.getValue());
            }
            currentRow++;
        } else {
            // This match has children, process them
            for (Match child : currentMatch.getChildren().values()) {
                calculate(child);
            }
        }

        // We're done with this match, remove it from the mapping
        currentMappings.remove(columnName);
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
