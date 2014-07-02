package nl.tudelft.wdm;

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

        PatternNode root = example2();

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

        System.out.println("Completed successfully");

        System.out.println();
        System.out.println("Printing matches:");
        System.out.println(eval.getRootMatch());

        // print result tuples
        System.out.println();
        System.out.println("Printing tuples:");
        ResultTupleCalculator resultTupleCalculator = new ResultTupleCalculator(eval.getRootMatch());
        ResultTupleCalculator.ResultList resultList = resultTupleCalculator.calculate();
        final SortedSet<String> columns = new TreeSet<>(resultList.getColumns());
        for (String column : columns) {
            if (resultTupleCalculator.getColumnNamesMap().inverse().get(column).isReturnResult()) {
                System.out.print(column);
                System.out.print("\t");
            }
        }
        System.out.println();
        for (Map<String, Integer> row : resultList.getResults()) {
            for (String column : columns) {
                if (resultTupleCalculator.getColumnNamesMap().inverse().get(column).isReturnResult()) {
                    final Integer value = row.get(column);
                    System.out.print((value == null ? "_" : value));
                    System.out.print("\t");
                }
            }
            System.out.println();
        }


        // print result xml
        System.out.println();
        System.out.println("Printing XML:");
        ResultXMLCalculator.printXmlNode(eval.getRootMatch(), 0);
    }

    private static PatternNode example1 () {
        // Define pattern here
        PatternNode root = new PatternNode.Builder("people").build();
        PatternNode person = new PatternNode.Builder("person", root)
                .makeReturnResult()
                .build();
        PatternNode id = new PatternNode.Builder("id", person)
                .makeAttributeNode()
                .makeOptional()
                .makeReturnResult()
                .build();

        return root;
    }

    private static PatternNode example2() {
        // Define pattern here
        PatternNode root = new PatternNode.Builder("people")
                .build();
        PatternNode person = new PatternNode.Builder("person", root)
                .makeReturnResult()
                .build();
        PatternNode id = new PatternNode.Builder("id", person)
                .makeAttributeNode()
                .makeOptional()
                .makeReturnResult()
                .build();
        PatternNode sex = new PatternNode.Builder("sex", person)
                .makeAttributeNode()
                .setValuePredicate("female")
                .makeReturnResult()
                .build();

        return root;
    }

    private static PatternNode example3() {
        // Define pattern here
        PatternNode root = new PatternNode.Builder("people")
                .build();
        PatternNode person = new PatternNode.Builder("person", root)
                .makeReturnResult()
                .build();
        PatternNode id = new PatternNode.Builder("id", person)
                .makeAttributeNode()
                .makeOptional()
                .makeReturnResult()
                .build();
        PatternNode wildcardAttr = new PatternNode.Builder("*", person)
                .makeAttributeNode()
                .makeOptional()
                .makeReturnResult()
                .build();
        PatternNode wildcardElem = new PatternNode.Builder("*", person)
                .makeOptional()
                .makeReturnResult()
                .build();

        return root;
    }

    private static PatternNode example4() {
        // Define pattern here
        PatternNode root = new PatternNode.Builder("people").build();
        PatternNode person = new PatternNode.Builder("person", root)
                .build();
        PatternNode id = new PatternNode.Builder("id", person)
                .makeAttributeNode()
                .makeOptional()
                .makeReturnResult()
                .build();
        PatternNode email = new PatternNode.Builder("email", person)
                .makeOptional()
                .makeReturnResult()
                .build();
        PatternNode name = new PatternNode.Builder("name", person)
                .makeReturnResult()
                .build();
        PatternNode fname = new PatternNode.Builder("first", name)
                .makeOptional()
                .makeReturnResult()
                .build();
        PatternNode lname = new PatternNode.Builder("last", name)
                .makeOptional()
                .makeReturnResult()
                .build();

        return root;
    }

}
