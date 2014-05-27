package nl.tudelft.wdm;

import com.google.common.collect.Table;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Ex1 {
    public static void main(String... args) {
        if (args.length < 1) {
            System.out.println("USAGE: Ex1 document.xml");
            return;
        }

        SAXParser sax;
        try {
            sax = SAXParserFactory.newInstance().newSAXParser();
        } catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
            System.err.println("Exception when creating SAX parser");
            return;
        }

        // Define pattern here
        PatternNode root = new PatternNode.Builder("people").build();
        PatternNode person = new PatternNode.Builder(root)
                .makeWildcardNode()
                .build();
        PatternNode email = new PatternNode.Builder("email", person)
                .makeOptional()
                .makeReturnResult()
                .build();
        PatternNode sex = new PatternNode.Builder("sex", person)
                .makeAttributeNode()
                .makeReturnResult()
                .build();

        StackEval eval = new StackEval(root);

        try {
            sax.parse(new File(args[0]), eval);
        } catch (FileNotFoundException e) {
            System.err.println(String.format("File %s doesn't exist", args[0]));
            return;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(String.format("Error when reading file %s", args[0]));
            return;
        } catch (SAXException e) {
            e.printStackTrace();
            System.err.println("Unhandled SAX exception while reading file.");
            return;
        }

        System.out.println("Completed successfully (niet hopen)");
        System.out.println(eval.getRootMatch());

        // print result tuples
        Table<Integer, String, Integer> tuples = new ResultTupleCalculator(eval.getRootMatch()).calculate();
        final SortedSet<String> columns = new TreeSet<>(tuples.columnKeySet());
        for (String column : columns) {
            System.out.print(column);
            System.out.print("\t");
        }
        System.out.println();
        for (Map<String, Integer> row : tuples.rowMap().values()) {
            for (String column : columns) {
                final Integer value = row.get(column);
                System.out.print((value == null ? "_" : value));
                System.out.print("\t");
            }
            System.out.println();
        }

        // print result xml
       printXmlNode(eval.getRootMatch(), 0);

    }

    public static void printXmlNode(Match match, int depth) {
        // print start element of match
        boolean attributesResult = false;
        if (match.getStack().getPatternNode().isReturnResult() || (attributesResult = attributesReturnResult(match.getStack().getPatternNode()))) {
            printIndentation(depth);
            System.out.print("<" + match.getName());
            // check for child attributes and print these within the xml tag.
            for (Match child : match.getChildren().values()) {
                if (child.getStack().getPatternNode().isAttribute()) {
                    System.out.print(" " + child.getName());
                    if (child.getTextValue() != null) {
                        System.out.print("=\"" + child.getTextValue() + "\"");
                    }
                }
            }
            System.out.print(">\n");
            if (match.getTextValue() != null) {
                printIndentation(depth+1);
                System.out.print(match.getTextValue() + "\n");
            }
            depth++;
        }

        // print children elements of match which are not attribute
        for (Match m : match.getChildren().values()) {
            if (!m.getStack().getPatternNode().isAttribute()) {
                printXmlNode(m, depth);
            }
        }

        // print end element of match
        if (match.getStack().getPatternNode().isReturnResult() || attributesResult) {
            printIndentation(depth-1);
            System.out.print("</" + match.getName() + ">\n");
        }
    }

    public static void printIndentation (int depth){
        for (int i = 0; i < depth; i++) {
            System.out.print("\t");
        }
    }

    public static boolean attributesReturnResult(PatternNode p) {
        for (PatternNode pChild : p.getChildren()) {
            if (pChild.isAttribute() && pChild.isReturnResult()) {
                return true;
            }
        }

        return false;
    }
}
