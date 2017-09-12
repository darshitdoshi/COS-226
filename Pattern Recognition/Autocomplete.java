import edu.princeton.cs.algs4.*;
import java.util.Arrays;

public class Autocomplete {
    
    private final Term[] terms;

    // Initializes the data structure from the given array of terms.
    // Sorts the terms in leexicographic order
    public Autocomplete(Term[] terms) {
        if(terms == null)
            throw new NullPointerException();
        
        Merge.sort(terms);
        this.terms = terms;
    }

    // Returns all terms that start with the given prefix, in descending order of weight.
    public Term[] allMatches(String prefix) {
        if (prefix == null)
            throw new NullPointerException();
        
        Term prefix_term = new Term(prefix, 0);
        
        int start = BinarySearchDeluxe.firstIndexOf(terms, prefix_term, Term.byPrefixOrder(prefix.length()));
        int end = BinarySearchDeluxe.lastIndexOf(terms, prefix_term, Term.byPrefixOrder(prefix.length()));
        
        //fetch the prefix-matched subarray between start and end
        //sort it in descending order of weight
        Term[] match = new Term[end - start + 1];
        match = Arrays.copyOfRange(terms, start, end+1);     
        MergeX.sort(match, Term.byReverseWeightOrder());
        return match;
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        if (prefix == null)
            throw new NullPointerException();
        
        Term prefix_term = new Term(prefix, 0);
        
        int start = BinarySearchDeluxe.firstIndexOf(terms, prefix_term, Term.byPrefixOrder(prefix.length()));
        int end = BinarySearchDeluxe.lastIndexOf(terms, prefix_term, Term.byPrefixOrder(prefix.length()));
        return end - start + 1;
        
    }
    
    
    public static void main(String[] args) {
    String filename = args[0];
    In in = new In(filename);
    int N = in.readInt();
    Term[] terms = new Term[N];
    for (int i = 0; i < N; i++) {
        long weight = in.readLong();           // read the next weight
        in.readChar();                         // scan past the tab
        String query = in.readLine();          // read the next query
        terms[i] = new Term(query, weight);    // construct the term
    }

    // read in queries from standard input and print out the top k matching terms
    int k = Integer.parseInt(args[1]);
    Autocomplete autocomplete = new Autocomplete(terms);
    while (StdIn.hasNextLine()) {
        String prefix = StdIn.readLine();
        Term[] results = autocomplete.allMatches(prefix);
        for (int i = 0; i < Math.min(k, results.length); i++)
            StdOut.println(results[i]);
    }
}
}