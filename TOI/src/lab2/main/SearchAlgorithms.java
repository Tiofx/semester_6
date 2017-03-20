package lab2.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchAlgorithms {

    private SearchAlgorithms() {
    }

    public static int interpolationSearch(int[] a, int key) {
        int low = 0, high = a.length - 1, mid;
        while (low <= high) {
            mid = low + (key - a[low]) / (a[high] - a[low]) * (high - low);
            if (a[mid] == key) {
                return mid;
            } else if (a[mid] > key) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    public static List<Integer> rabinSearch(String text, String key) {
        List<Integer> result = new ArrayList<>();
        StringHash textHash, keyHash;
        textHash = new StringHash(text, key.length());
        keyHash = new StringHash(key);

        textHash.calculateHash();
        keyHash.calculateHash();

        while (textHash.hasNextHash()) {
            if (textHash.getHash() == keyHash.getHash()) {
                result.add(textHash.getStartPosition());
            }
            textHash.nextHash();
        }

        if (result.size() > 0) {
            return result;
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}
