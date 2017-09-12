import edu.princeton.cs.algs4.*;
import java.util.HashMap;

public class WordNet {
    // synsets - key:synset id, field:bag of corresponding nouns 
    private HashMap<Integer, Bag<String>> synsets = new HashMap<Integer, Bag<String>>();
    // nouns - key:noun, field:bag of corresponding synset id
    private HashMap<String, Bag<Integer>> nouns = new HashMap<String, Bag<Integer>>(); 
    private Digraph wordnet_graph;
    private ShortestCommonAncestor shortest_common_ancestor;
    
   // constructor takes the name of the two input files
    public WordNet(String synsets_filename, String hypernyms_filename) {
        In synsets_file = new In(synsets_filename);
        int lines = 0;
        while(synsets_file.hasNextLine()) {
            lines++;
            String line = synsets_file.readLine();
            String[] line_split = line.split(",");
            int synsets_id = Integer.parseInt(line_split[0]);
            String[] synsets_nouns = line_split[1].split(" "); 
            
            for(String word : synsets_nouns)
            {
                // add each noun to 'synsets' corresponding to its id
                Bag<String> noun_list = synsets.get(synsets_id);
                if(noun_list == null) {
                    noun_list = new Bag<String>();
                    noun_list.add(word);
                    synsets.put(synsets_id, noun_list);
                }
                else {
                    noun_list.add(word);
                }
                // add the current id to 'nouns' for the corresponding noun
                Bag<Integer> id_list = nouns.get(word);
                if(id_list == null) {
                    id_list = new Bag<Integer>();
                    id_list.add(synsets_id);
                    nouns.put(word, id_list);
                }
                else {
                    id_list.add(synsets_id);
                }
                
            }
        }
        
        // generate a digraph of size correspondig to the lines in synsets file
        // generate the wordnet graph using hypernym file
        wordnet_graph = new Digraph(lines);
        In hypernyms_file = new In(hypernyms_filename);
        while(hypernyms_file.hasNextLine()) {
            String line = hypernyms_file.readLine();
            String[] line_split = line.split(",");
            int synsets_id = Integer.parseInt(line_split[0]);
            for(int i = 1; i < line_split.length; i++) {
                wordnet_graph.addEdge(synsets_id, Integer.parseInt(line_split[i]));
            }
        }
        
        shortest_common_ancestor = new ShortestCommonAncestor(wordnet_graph);
    }

   // all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

   // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nouns.containsKey(word);
    }

   /* a synset (second field of synsets.txt) that is a shortest common ancestor
    * of noun1 and noun2 
    */
    public String sca(String noun1, String noun2) {
        if((!isNoun(noun1)) || (!isNoun(noun2)))
            throw new java.lang.IllegalArgumentException("Nouns are invalid");
        
        Bag<Integer> v = nouns.get(noun1);
        Bag<Integer> w = nouns.get(noun2);
        int ancestor_id = shortest_common_ancestor.ancestor(v, w);
        Bag<String> ancestor_list = synsets.get(ancestor_id);
        String ancestor = new String();
        int count = 0;
        for(String word : ancestor_list) {
            // generating the ancestor string in the required format
            count ++;
            ancestor = ancestor.concat(word);
            if(count < ancestor_list.size())
                ancestor = ancestor.concat(" ");
        }
        return ancestor;
    }

   // distance between noun1 and noun2 
    public int distance(String noun1, String noun2) {
        if((!isNoun(noun1)) || (!isNoun(noun2)))
            throw new java.lang.IllegalArgumentException("Nouns are invalid");
        
        Bag<Integer> v = nouns.get(noun1);
        Bag<Integer> w = nouns.get(noun2);
        
        return shortest_common_ancestor.length(v, w);
    }

   // unit testing of this class
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wordnet = new WordNet(synsets, hypernyms);
        StdOut.println("Enter noun1 and noun2");
        String noun1 = StdIn.readString();
        String noun2 = StdIn.readString();
        StdOut.printf("The ancestor of %s and %s is %s\nAnd shortest distance = %d\n",noun1, noun2, wordnet.sca(noun1, noun2), wordnet.distance(noun1, noun2));
    }
}