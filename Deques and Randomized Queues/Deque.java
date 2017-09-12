import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    
    //Deque has been implemented using doubly linked list
    
    /* first - first item of deque
     * last - last item of deque
     * n - no. of items in deuqe
     */
    
    private Node first;
    private Node last;
    private int n;
    
    private class Node { 
        private Item item;
        private Node next;
        private Node previous;
        
    }
    
    // construct an empty deque
    public Deque() {                          
        first = new Node();
        last = new Node();
        first = last;
        n = 0;
    }
    
    // check if the deque is empty
    public boolean isEmpty() {                
        return(n==0);
    }
    
    // return the number of items on the deque
    public int size() {                       
        return n;
    }
    
    // add the item to the front
    public void addFirst(Item item) {          
        if(item == null)
            throw new NullPointerException();
        n++;
        Node temp = new Node();
        temp.item = item;
                if(n == 1) {                  //check if it is the first item
            last = first = temp;
            return;
        }
        first.previous = temp;
        temp.next = first;
        first = temp;
    }
    
    // add the item to the end
    public void addLast(Item item) {           
        if(item == null)
            throw new NullPointerException();
        n++;
        Node temp = new Node();
        temp.item = item;
        if(n == 1) {                           //check if it is the first item
            last = first = temp;
            return;
        }
        temp.previous = last;
        last.next = temp;
        last = temp;
    }
    
    // remove and return the item from the front
    public Item removeFirst() {               
        if(isEmpty())
            throw new NoSuchElementException();
        n--;
        Item item = first.item;
        if(n == 0) {                         //check if it is the last item
            last = first = null;
            return item;
        }
        first = first.next;
        first.previous = null;
        return item;
    }
    
    // remove and return the item from the end
    public Item removeLast() {                 
        if(isEmpty())
            throw new NoSuchElementException(); 
        n--;
        Item item = last.item;
        if(n == 0) {                          //check if it is the last item
            last = first = null;
            return item;
        }
        last = last.previous;
        last.next = null;
        return item;       
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {        
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        
        public boolean hasNext() {
            return (current != null);
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public Item next() {
            if(!hasNext())
                throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    // unit testing
    public static void main(String[] args) {   
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(10);
        deque.addFirst(11);
        deque.addLast(20);
        deque.removeFirst();
        deque.removeLast();
        deque.addLast(30);
        //deque.removeLast();
        //deque.removeLast();
        deque.addFirst(20);
        Iterator iter = deque.iterator();
        while(iter.hasNext())
            StdOut.print(iter.next() + "  ");
        StdOut.println();
    }
    
}
