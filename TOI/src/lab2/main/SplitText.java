package lab2.main;

public class SplitText {

    public String[] split(String text, SplitScale scale) {
        return text.split(scale.getSplitRegex());
    }
}
