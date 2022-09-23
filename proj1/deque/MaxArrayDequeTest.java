package deque;

import afu.org.checkerframework.checker.oigj.qual.O;
import net.sf.saxon.functions.Minimax;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    @Test
    public void testInt() {
        MaxArrayDeque<Integer> testDeque = new MaxArrayDeque<>(new intComparator());
        testDeque.addLast(3);
        testDeque.addLast(-1);
        testDeque.addLast(6);
        testDeque.addLast(0);
        testDeque.addLast(7);
        assertEquals((int)testDeque.max(), 7);
    }

    @Test
    public void testString() {
        MaxArrayDeque<String> testDeque = new MaxArrayDeque<>(new stringComparator());
        testDeque.addFirst("I");
        testDeque.addFirst("love");
        testDeque.addFirst("java");
        testDeque.addFirst("yes");
        assertEquals(testDeque.max(), "java");
    }

    private static class intComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }

    private static class stringComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }
}
