package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author GreatGagarin
 */
public class ArrayDequeTest {
    @Test
    public void randomizedTest() {
        LinkedListDeque<Integer> L = new LinkedListDeque<>();
        ArrayDeque<Integer> T = new ArrayDeque<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 5);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                T.addLast(randVal);
                assertEquals(L.size(), T.size());
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                L.addFirst(randVal);
                T.addFirst(randVal);
                assertEquals(L.size(), T.size());
                System.out.println("addFirst(" + randVal + ")");
            }else if (operationNumber == 2) {
                assertEquals(L.removeLast(), T.removeLast());
            } else if (operationNumber == 3) {
                assertEquals(L.removeFirst(), T.removeFirst());
            } else if (operationNumber == 4) {
                assertEquals(L.size(), T.size());
            }
        }
    }
}
