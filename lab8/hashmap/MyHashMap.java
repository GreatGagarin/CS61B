package hashmap;

import afu.org.checkerframework.checker.igj.qual.I;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author Great Gagarin
 */
public class MyHashMap<K, V> implements Map61B<K, V> {


    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private static final int INITIAL_SIZE = 16;
    private static final double INITIAL_MAX_LOAD_FACTOR = 0.75;
    private Collection<Node>[] buckets;
    private int size;
    private final double maxLoadFactor;

    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(INITIAL_SIZE, INITIAL_MAX_LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, INITIAL_MAX_LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        size = 0;
        maxLoadFactor = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i += 1) {
            table[i] = createBucket();
        }
        return table;
    }

    @Override
    public void clear() {
        buckets = createTable(INITIAL_SIZE);
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key){
       Node node = getNode(key);
       if (node != null) {
           return node.value;
       }
       return null;
    }

    private Node getNode(K key){
        int index = getIndex(key);
        for (Node node : buckets[index]) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value){
        int index = getIndex(key);
        Node currentNode = createNode(key, value);
        Node remainNode = getNode(key);
        if (remainNode != null) {
            remainNode.value = value;
        } else {
            buckets[index].add(currentNode);
            size += 1;
        }
        if (isLoadFactor()) {
            resize(buckets.length * 2);
        }
    }

    private boolean isLoadFactor() {
        double loadFactor = (double) size / buckets.length;
        return loadFactor >= maxLoadFactor;
    }

    private void resize(int bucktSize) {
        Collection<Node>[] resizedBuckts = createTable(bucktSize);
        for (Collection<Node> temp : buckets) {
            for (Node node : temp) {
                int index = Math.abs(node.key.hashCode() % bucktSize);
                resizedBuckts[index].add(node);
            }
        }
        buckets = resizedBuckts;
    }
    @Override
    public Set<K> keySet(){
        Set<K> set = new HashSet<>();
        for (K key : this) {
            set.add(key);
        }
        return set;
    }

    @Override
    public V remove(K key){
        Node node = getNode(key);
        if (node == null) {
            return null;
        } else {
            int index = getIndex(key);
            buckets[index].remove(node);
            return node.value;
        }
    }

    @Override
    public V remove(K key, V value){
        Node node = getNode(key);
        if (node == null || node.value != value) {
            return null;
        } else {
            int index = getIndex(key);
            buckets[index].remove(node);
            return node.value;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {

        private final Iterator<Collection<Node>> bucketIterator = Arrays.stream(buckets).iterator();
        private Iterator<Node> nodeIterator;
        private int remainSize = size;

        @Override
        public boolean hasNext() {
            return remainSize > 0;
        }

        @Override
        public K next() {
            if (nodeIterator == null || !nodeIterator.hasNext()) {
                Collection<Node> currentBucket = bucketIterator.next();
                while (currentBucket.size() == 0) {
                    currentBucket = bucketIterator.next();
                }
                nodeIterator = currentBucket.iterator();
            }
            remainSize -= 1;
            return nodeIterator.next().key;
        }
    }
    private int getIndex(K key){
        return Math.abs(key.hashCode() % buckets.length);
    }

}
