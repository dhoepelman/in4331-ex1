package nl.tudelft.wdm;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
        PatternNode root = new PatternNode("people");
        new PatternNode("person", root);

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
    }
}
