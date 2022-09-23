package deque;

import java.awt.desktop.PreferencesEvent;

/**
 * @author GreaGagarin
 * @param <Item>
 */
public class ArrayDeque<Item> implements Deque<Item> {
    private Item[] items;
    private int nextFirst, nextLst;
    private int size;

    public ArrayDeque() {
        items = (Item[]) new Object[8];
        nextFirst = 0;
        nextLst = 1;
        size = 0;
    }

    public boolean isFull() {
        if ((nextLst + 1) % items.length == nextFirst) {
            return true;
        }
        return false;
    }

    private void resize(int capacity) {
        Item[] a = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i += 1) {
            nextFirst = (nextFirst + 1) % items.length;
            a[i] = items[nextFirst];
        }
        items = a;
        nextFirst = items.length - 1;
        nextLst = size;
    }

    @Override
    public void addFirst(Item i) {
        if (isFull()) {
            resize(items.length * 2);
        }
        items[nextFirst] = i;
        nextFirst = (nextFirst - 1 + items.length) % items.length;
        size += 1;
    }

    @Override
    public void addLast(Item i) {
        if (isFull()) {
            resize(items.length * 2);
        }
        items[nextLst] = i;
        nextLst = (nextLst + 1) % items.length;
        size += 1;
    }

    @Override
    public Item removeFirst() {
        if ((size + 2) < items.length / 4 && items.length > 16) {
            resize(items.length / 4);
        }
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        nextFirst = (nextFirst + 1) % items.length;
        return items[nextFirst];
    }
    @Override
    public Item removeLast() {
        if ((size + 2) < items.length / 4 && items.length > 16) {
            resize(items.length / 4);
        }
        if (isEmpty()) {
            return null;
        }
        size -= 1;
        nextLst = (nextLst - 1 + items.length) % items.length;
        return items[nextLst];
    }

    @Override
    public Item get(int index) {
        return items[(nextFirst + index + 1) % items.length];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int index = (nextFirst = 1) % items.length;
        while (index != nextLst) {
            System.out.print(items[index] + " ");
            index = (index + 1) % items.length;
        }
        System.out.print("\n");
    }
}