package nl.tudelft.wdm;

public class Match {
    // Original Map <PatternNode, Array<Match>> children; , but Array isn't a class in Java...
    // I guess Map <PatternNode, List<Match>> was meant, and Guava has a MultiMap that is abstracts Map<P, List<Q>>
    private MutliMap<PatternNode, Match> children;
    private TPEStack st;
    private int start;
    private STATUS status;
    private Match parent;

    public STATUS getStatus() {
        return status;
    }

    public enum STATUS {
        OPEN,
        CLOSED
    }
}
