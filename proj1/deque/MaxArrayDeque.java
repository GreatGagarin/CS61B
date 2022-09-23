package deque;

import net.sf.saxon.om.Item;
import java.util.Comparator;

public class MaxArrayDeque<Item> extends ArrayDeque<Item> {

    public Comparator<Item> dequeComparator;

    public MaxArrayDeque(Comparator<Item> c){
        dequeComparator = c;
    }

    public Item max() {
        return max(dequeComparator);
    }

    public Item max(Comparator<Item> c) {
        if (isEmpty()) {
            return null;
        }
        int maxIndex = 0;
        for(int i = 0; i < size(); i += 1) {
            if (c.compare(get(i), get(maxIndex)) > 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }

}
