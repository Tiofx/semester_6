package lab3.main;

public class WordStatistic {
    protected int rang;
    protected int number;
    protected float frequency;

    public WordStatistic(int rang, int number, float frequency) {
        this.rang = rang;
        this.number = number;
        this.frequency = frequency;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
    }
}
