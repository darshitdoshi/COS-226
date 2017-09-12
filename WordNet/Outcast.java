import edu.princeton.cs.algs4.*;

public class Outcast {
    
    private WordNet wordnet;
    
   // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int max_dist = 0;
        String outcast_noun = null;
        for(String nounA : nouns) {
            int current_distance = 0;
            for(String nounB : nouns) {
                current_distance += wordnet.distance(nounA, nounB);
            }
            if(current_distance > max_dist) {
                max_dist = current_distance;
                outcast_noun = nounA;
            }
        }
        return outcast_noun;
        
    }
    
    // unit testing of class
    public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
    }
}