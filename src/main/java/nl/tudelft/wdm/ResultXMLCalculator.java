package nl.tudelft.wdm;

public class ResultXMLCalculator {
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
            if (match.getTextValue() != null && !match.getTextValue().trim().equals("")) {
                printIndentation(depth + 1);
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
            printIndentation(depth - 1);
            System.out.print("</" + match.getName() + ">\n");
        }
    }

    public static void printIndentation(int depth) {
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