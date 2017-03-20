package lab2.util;

import java.util.HashMap;
import java.util.Set;

public abstract class AbstractVariant {
    protected String filename = "table.txt";

    public AbstractVariant() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public abstract HashMap<Character, Integer> getAlphabet();

    public abstract Set<Integer> getFinalStates();

    public int getFirstState() {
        return 0;
    }

    public int[][] getTransitionTable() {
        return getTransitionTable(filename);
    }

    public int[][] getTransitionTable(String filename) {
        return Parser.toArray(filename);
    }
}
