package deque;

import afu.org.checkerframework.checker.igj.qual.I;


/**
 * @author GreatGagarin
 * @param <Item>
 */
public class LinkedListDeque<Item> implements Deque<Item> {
    private LinkedListNode sentinel;
    private int size;
    public class LinkedListNode {
        public Item item;
        public LinkedListNode next;
        public LinkedListNode prev;

        public LinkedListNode(Item i, LinkedListNode n, LinkedListNode p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    public LinkedListDeque() {
        sentinel = new LinkedListNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(Item i) {
        LinkedListNode temp = new LinkedListNode(i, sentinel.next, sentinel);
        sentinel.next = temp;
        temp.next.prev = temp;
        size += 1;
    }

    @Override
    public void addLast(Item i) {
        LinkedListNode temp = new LinkedListNode(i, sentinel, sentinel.prev);
        sentinel.prev = temp;
        temp.prev.next = temp;
        size += 1;
    }

    @Override
    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
        LinkedListNode temp = sentinel.next;
        while (temp != sentinel) {
            System.out.print(temp.item + " ");
            temp = temp.next;
        }
        System.out.print("\n");
    }

    @Override
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            LinkedListNode temp = sentinel.next;
            sentinel.next = temp.next;
            temp.next.prev = sentinel;
            size -= 1;
            return temp.item;
        }
    }

    @Override
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            LinkedListNode temp = sentinel.prev;
            sentinel.prev = temp.prev;
            temp.prev.next = sentinel;
            size -= 1;
            return temp.item;
        }
    }

    @Override
    public Item get(int index) {
        if (index > size - 1) {
            return null;
        } else {
            LinkedListNode temp = sentinel.next;
            for (int i = 0; i < index; i += 1) {
                temp = temp.next;
            }
            return temp.item;
        }
    }

    private Item getRecursive(LinkedListNode node, int index) {
        if (index == 0) {
            return node.item;
        } else {
            return getRecursive(node.next, index - 1);
        }
    }

    public Item getRecursive(int index) {
        if (index > size - 1) {
            return null;
        } else {
            return getRecursive(sentinel.next, index);
        }
    }
}