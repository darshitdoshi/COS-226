import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;
public class BinarySearchDeluxe {

    // Returns the index of the first key in array[] that equals the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] array, Key key, Comparator<Key> comparator) {
        if(array == null || key == null || comparator == null)
            throw new NullPointerException();
        
        if(array.length == 0)
            return -1;
        
        
        int start = 0;
        int end = array.length-1;
        int mid = 0;
        //Perform binary search
        while(start+1 < end) {
            mid = (start + end) / 2;
            if(comparator.compare(array[mid], key) >= 0)
                end = mid;
            else
                start = mid;
        }
        if(comparator.compare(array[start], key) == 0)
            return start;
        if(comparator.compare(array[end], key) == 0)
            return end;

        return -1;
    }

    // Returns the index of the last key in array[] that equals the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] array, Key key, Comparator<Key> comparator) {
        if(array == null || key == null || comparator == null)
            throw new NullPointerException();
        
        if(array.length == 0)
            return -1;
        
        int start = 0;
        int end = array.length-1;
        int mid = 0;
        //Perform binary search
        while(start+1 < end) {
            mid = (start + end) / 2;
            if(comparator.compare(array[mid], key) > 0)
                end = mid;
            else
                start = mid;    
        }
        if(comparator.compare(key, array[end]) == 0)
            return end;
        if(comparator.compare(key, array[start]) == 0)
            return start;
        
        return -1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Term[] t1 = new Term[4];
        t1[0] = new Term("abcd", 10);
        t1[1] = new Term("haha", 20);
        t1[2] = new Term("haha", 10);
        t1[3] = new Term("x", 10);
        Term find = new Term("ha", 20);
        int first = firstIndexOf(t1, find, Term.byPrefixOrder(3));
        StdOut.println(first);
        int last = lastIndexOf(t1, find, Term.byPrefixOrder(3));
        StdOut.println(last);
        
    }
}