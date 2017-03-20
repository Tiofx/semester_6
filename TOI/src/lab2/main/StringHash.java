package lab2.main;

public class StringHash {
    protected String text;
    protected double hash = 0;
    protected int i = 0, n;

    public StringHash(String string) {
        this(string, string.length());
    }

    public StringHash(String text, int n) {
        this.text = text;
        this.n = n;
    }

    public double calculateHash() {
        hash = 0;
        for (int j = 0; j < n; j++) {
            hash = 2 * hash + text.charAt(j + i);
        }
        i = n - 1;

        return hash;
    }

    public double nextHash() {
        i++;
        int c = text.charAt(i - n) << (n - 1);
        hash = 2 * (hash - c) + text.charAt(i);

        return hash;
    }

    public boolean hasNextHash() {
        return text.length() - (i + 1) > n;
    }

    public double getHash() {
        return hash;
    }

    public int getStartPosition() {
        return i - n + 1;
    }
}
