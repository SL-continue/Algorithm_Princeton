import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item [] items;
    private int size;
    private int capacity;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        capacity = 2;
        items = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    private void resize() {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = items[i];
        }
        items = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("Enqueue null item");
        if (size == capacity) {
            capacity = capacity * 2;
            resize();
        }
        items[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) throw new java.util.NoSuchElementException("Queue empty");
        int randomChoice = StdRandom.uniformInt(size);
        Item item = items[randomChoice];
        items[randomChoice] = items[size - 1];
        items[size - 1] = null;
        size--;
        if (size > 0 && size == capacity / 4) {
            capacity = capacity / 2;
            resize();
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) throw new java.util.NoSuchElementException("Queue empty");
        return items[StdRandom.uniformInt(size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedIterator(size);
    }

    private class RandomizedIterator implements Iterator<Item> {
        private final int [] randomOrder;
        private int current;

        public RandomizedIterator(int size) {
            randomOrder = StdRandom.permutation(size);
            current = 0;
        }

        public boolean hasNext() {
            return current < randomOrder.length;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("No next item");
            Item item = items[randomOrder[current]];
            current++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomQue = new RandomizedQueue<Integer>();
        int [] toInsert = {1, 1, 2, 3, 5, 8, 13, 21, 34};
        for (int element : toInsert) {
            randomQue.enqueue(element);
        }
        if (randomQue.isEmpty()) StdOut.println("Elements not enqueued");
        for (Integer element : randomQue) {
            StdOut.println(element);
        }
        StdOut.println(String.format("random pick %d", randomQue.sample()));
        StdOut.println(String.format("size before removal is %d", randomQue.size()));
        StdOut.println(String.format("first to be removed is %d", randomQue.dequeue()));
        StdOut.println(String.format("size after removal is %d", randomQue.size()));
        for (Integer element : randomQue) {
            StdOut.println(element);
        }
    }

}
