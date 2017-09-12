import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

public class Term implements Comparable<Term> {

    private String query;
    private long weight;
    
    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if(query == null)
            throw new NullPointerException("Null query");
        if(weight < 0)
            throw new IllegalArgumentException("Negative weight");
        
        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new Comparator<Term>() {
            public int compare(Term t1, Term t2) {
                if(t1.weight > t2.weight)
                    return -1;
                else if(t1.weight == t2.weight)
                    return 0;
                else
                    return 1;
            }
        };
    }

    // Compares the two terms in lexicographic order but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int R) {
        if(R < 0)
            throw new IllegalArgumentException("r is negative");
        
        final int r = R;
        return new Comparator<Term>() {
            public int compare(Term t1, Term t2) {
                String s1 = t1.query;
                String s2 = t2.query;
                
                if(r <= s1.length() && r <= s2.length())
                    return s1.substring(0, r).compareTo(s2.substring(0, r));
                
                else {
                    int min_len = s1.length() < s2.length() ? s1.length() : s2.length();
                    return s1.substring(0, min_len).compareTo(s2.substring(0, min_len));
                }
                    
                
            }
            
        };
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        return(query.compareTo(that.query));
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return("" + weight + "\t" + query);
    }

    // unit testing (required)
    public static void main(String[] args) {
        Term t1 = new Term("haha", 10);
        Term t2 = new Term("abc", 20);
        
        StdOut.println(t1.toString());
        StdOut.println(t1.compareTo(t2));
        StdOut.println(byReverseWeightOrder().compare(t2, t1));
        StdOut.println(byPrefixOrder(3).compare(t1, t2));
    }
}