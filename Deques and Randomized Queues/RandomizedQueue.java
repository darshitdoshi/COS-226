import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item[] queue;  //randomized queue
    private int n;         //no. of items
    
     // construct an empty randomized queue of length 1
    public RandomizedQueue() {                
        queue = (Item[]) new Object[1]; 
    }
    
    // check if the queue is empty
    public boolean isEmpty() {                
        return(n == 0);
    }
    
    // return the number of items on the queue
    public int size() {                       
        return n;
    }
    
    // add the item, resize if necessary
    public void enqueue(Item item) {          
        if(item == null)
            throw new NullPointerException();
        if(n == queue.length)
            resize(2 * queue.length);
        queue[n++] = item;
    }
    
    // remove and return a random item, resize if necessary
    public Item dequeue() {                   
        if(isEmpty())
            throw new NoSuchElementException();
        int i = StdRandom.uniform(n);
        Item value = queue[i];
        queue[i] = queue[--n];      //replacing the dequeued item with last item
        queue[n] = null;
        
        if(n < queue.length / 4)
            resize(queue.length / 2);
        return value;
    }
    
    // returns a random item (but does not remove it)
    public Item sample() {                    
        if(isEmpty())
            throw new NoSuchElementException();
        return queue[StdRandom.uniform(n)];
    }
    
    //resize the queue to capacity
    public void resize(int capacity) {        
        Item[] temp = (Item[]) new Object[capacity];
        for(int i = 0; i < n; i++)
            temp[i] = queue[i];
        queue = temp;
    }
    
    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {        
        return new RandomizedQueueIterator();
    }
    
    //implementing an independent iterator over items in random order
    private class RandomizedQueueIterator implements Iterator<Item> {
        
        private int index = 0;
        
        /*The queue has length of power of 2 due to resize() 
         * Hence we need to remove the null items if any,
         * before randomly shuffling its elements
         */
        public RandomizedQueueIterator() {
            Item[] temp = (Item[]) new Object[n];
            for(int i = 0; i < n; i++)
                temp[i] = queue[i];
            queue = temp;
            StdRandom.shuffle(queue);
        }
        
        public boolean hasNext() {
            return index < n;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        public Item next() {
            if(!hasNext())
                throw new NoSuchElementException();
            return queue[index++];
        }
        
    }
    
    //print the queue
    public void printQueue(RandomizedQueue<Item> rq) {   
        Iterator iter = rq.iterator();
        while(iter.hasNext())
            StdOut.print(iter.next() + "  ");
        StdOut.println();
    }
    
    // unit testing
    public static void main(String[] args) {            
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(29);
        rq.enqueue(12);
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(45);
        rq.enqueue(3);
        rq.enqueue(90);
        rq.enqueue(5);
        rq.enqueue(96);
        
        rq.printQueue(rq);
        StdOut.println(rq.dequeue());
        //StdOut.println(rq.dequeue());
        rq.enqueue(50);
        StdOut.println(rq.sample());
        
        rq.printQueue(rq);
        
    }
}