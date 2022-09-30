package bstmap;

import edu.princeton.cs.algs4.BST;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K,V>{

    private class BSTNode{
        K key;
        V value;
        public BSTNode lchild;
        public BSTNode rchild;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.lchild = null;
            this.rchild = null;
        }
    }

    private BSTNode BSTree;
    private int size;

    public BSTMap() {
        BSTree = null;
        size = 0;
    }

    @Override
    public void clear() {
        BSTree = null;
        size = 0;
    }

    private boolean containsKey(K key, BSTNode root) {
        if (root == null) {
            return false;
        }
        int cmp = root.key.compareTo(key);
        if (cmp == 0) {
            return true;
        } else if (cmp < 0) {
            return containsKey(key, root.rchild);
        } else {
            return containsKey(key, root.lchild);
        }
    }

    @Override
    public boolean containsKey(K key){
        return containsKey(key, BSTree);
    }

    private V get(K key, BSTNode root) {
        if (root == null) {
            return null;
        }
        int cmp = root.key.compareTo(key);
        if (cmp == 0) {
            return root.value;
        } else if (cmp < 0) {
            return get(key, root.rchild);
        } else {
            return get(key, root.lchild);
        }
    }
    @Override
    public V get(K key) {
        return get(key, BSTree);
    }

    @Override
    public int size() {
        return size;
    }

    private BSTNode put(K key, V value, BSTNode root) {
        if (root == null) {
            return new BSTNode(key, value);
        }
        int cmp = root.key.compareTo(key);
        if (cmp == 0) {
            root.value = value;
        }
        if (cmp < 0) {
            root.rchild = put(key, value, root.rchild);
        } else {
            root.lchild = put(key, value, root.lchild);
        }
        return root;
    }
    @Override
    public void put(K key, V value) {
        BSTree = put(key, value, BSTree);
        size += 1;
    }

    @Override
    public Set<K> keySet() {
        HashSet<K> set = new HashSet<>();
        addSet(BSTree, set);
        return set;
    }

    private void addSet(BSTNode root, Set<K> set) {
        if (root == null) {
            return;
        }
        set.add(root.key);
        addSet(root.lchild, set);
        addSet(root.rchild, set);
    }

    @Override
    public V remove(K key) {
        V targetValue = get(key);
        if (targetValue != null) {
            BSTree = remove(BSTree, key);
            size -= 1;
        }
        return targetValue;
    }

    private BSTNode remove(BSTNode root, K key) {
        if (root == null) {
            return root;
        }
        int cmp = root.key.compareTo(key);
        if (cmp < 0) {
            root.rchild = remove(root.rchild, key);
        } else if (cmp > 0) {
            root.lchild = remove(root.lchild, key);
        } else {
            root = remove(root);
        }
        return root;
    }

    private BSTNode remove(BSTNode root) {
        if (root.lchild == null) {
            return root.rchild;
        } else if (root.rchild == null) {
            return root.lchild;
        } else {
            BSTNode maxNode = getMaxNode(root.lchild);
            maxNode.lchild = root.lchild;
            maxNode.rchild = root.rchild;
            maxNode.lchild = remove(maxNode.lchild, maxNode.key);
            return maxNode;
        }
    }

    private BSTNode getMaxNode(BSTNode root) {
        if (root.rchild != null) {
            return getMaxNode(root.rchild);
        } else {
            return new BSTNode(root.key, root.value);
        }
    }

    @Override
    public V remove(K key, V value) {
        V targetValue = get(key);
        if (targetValue.equals(value)) {
            BSTree = remove(BSTree, key);
            size -= 1;
            return targetValue;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    private void printInOrder(BSTNode root) {
        if (root == null) {
            return;
        }
        printInOrder(root.lchild);
        System.out.print(root.key + " ");
        printInOrder(root.rchild);
    }
    public void printInOrder() {
        printInOrder(BSTree);
    }
}
