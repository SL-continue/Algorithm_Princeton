import java.util.Iterator;
import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {

    private class Node<Item> {
        Item item;
        Node<Item> next;
        Node<Item> prev;
    }

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    private void checkItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("item is null");
        }
    }

    private void checkEmpty() {
        if (size == 0) {
            throw new java.util.NoSuchElementException("Deque is empty");
        }
    }

    // add the item to the front
    public void addFirst(Item item) {
        checkItem(item);
        Node<Item> curFirst = first;
        first = new Node<Item>();
        first.item = item;
        first.next = curFirst;
        first.prev = null;
        if (size == 0) last = first;
        else curFirst.prev = first;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        checkItem(item);
        Node<Item> curLast = last;
        last = new Node<Item>();
        last.item = item;
        last.next = null;
        if (size == 0) first = last;
        else {
            curLast.next = last;
            last.prev = curLast;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        checkEmpty();
        Item item = first.item;
        first = first.next;
        size--;
        if (size == 0) last = first;
        else first.prev = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        checkEmpty();
        Item item = last.item;
        last = last.prev;
        size--;
        if (size == 0) first = last;
        else last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current;

        public DequeIterator(Node<Item> first) {
            current = first;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException("No next item");
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        int [] toInsertFront = {5, 3, 2, 1, 1};
        int [] toInsertBack = {8, 13, 21, 34};
        for (int element : toInsertFront) {
            deque.addFirst(element);
        }
        for (int element : toInsertBack) {
            deque.addLast(element);
        }
        if (deque.isEmpty()) StdOut.println("Elements not inserted");
        for (Integer element : deque) {
            StdOut.println(element);
        }
        StdOut.println(String.format("size before removal is %d", deque.size()));
        StdOut.println(String.format("first to be removed is %d", deque.removeFirst()));
        StdOut.println(String.format("last to be removed is %d", deque.removeLast()));
        StdOut.println(String.format("size after removal is %d", deque.size()));
        for (Integer element : deque) {
            StdOut.println(element);
        }
    }

}
