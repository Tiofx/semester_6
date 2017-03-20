package lab2.main;

import java.util.Arrays;

public class Task1 {
    protected int n;
    protected int maxValue;
    protected int mas[];

    public Task1(int n, int maxValue) {
        this.n = n;
        this.maxValue = maxValue;

        mas = new int[n];
        generateArray();
    }

    public Task1() {
        this(1000, 10000);
    }

    public void generateArray() {
        for (int i = 0; i < n; i++) {
            mas[i] = (int) (Math.random() * maxValue);
        }

        Arrays.sort(mas);
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;

        mas = new int[n];
        generateArray();
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int[] getMas() {
        return mas;
    }

    @Override
    public String toString() {
        return Arrays.stream(mas)
                .mapToObj(String::valueOf)
                .reduce("", (s1, s2) -> s1 + " " + s2)
                .trim();
    }
}
