package nl.tudelft.wdm;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A parser for a very, very simple subset of XQuery
 * Supported syntax: QUERY
 * QUERY = "
 * for $[a-z]+ in " EXPR "
 * (where (" TOPEXPR "=.+)*
 * (return \((" TOPEXPR ",?)+\) )"
 * TOPEXPR = ($[a-z]+)? " EXPR "
 * EXPR = ""
 * EXPR = "/[a-z*]+" ATTR EXPR
 * ATTR = ""
 * ATTR = "\[[a-z]+(=[^\]]+)\]" ATTREXPR
 * WHERE
 * <p/>
 * If return in not specified, all involved nodes will be returned
 */
public class SimplyQueryParser {
    private final String REGEX_VARIABLE = "\\$[a-z]+";
    private final String REGEX_ATTR = "(\\[[a-z]+(=[^\\]]+)?\\])";
    private final String REGEX_EXPR = "(/[a-z\\*]+(" + REGEX_ATTR + ")*)+";
    private final String REGEX_TOPEXPR = "(" + REGEX_VARIABLE + ")?" + REGEX_EXPR;
    Scanner queryScanner;
    String token;
    /**
     * The variable from the for part of the expression
     */
    String variable;

    public PatternNode parse(String query) {
        queryScanner = new Scanner(query);
        consume("for");
        variable = consume(REGEX_VARIABLE).substring(1);
        consume("in");
        String variableExpr = consume(REGEX_EXPR);

    }

    private String consume(String regex) {
        try {
            token = queryScanner.next(regex);
        } catch (NoSuchElementException e) {
            illegal();
        }
        return token;
    }

    private void illegal() {
        throw new IllegalArgumentException(String.format("Illegal token %s", token));
    }
}
