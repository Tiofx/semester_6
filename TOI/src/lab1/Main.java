package lab1;

import lab1.gui.InputForm;
import lab2.main.SplitScale;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class Main {

    public static void main(String[] args) {
        create(null);
    }

    public static void create(Component parentComponent) {
        JOptionPane.showMessageDialog(parentComponent,
                new InputForm(),
                "Лабораторная работа 1",
                JOptionPane.PLAIN_MESSAGE);
    }


    public static class Lab1 {
        protected String input;
        protected int filterLength;

        protected String splitString = SplitScale.BY_WORDS.getSplitRegex();
        protected boolean caseInsensitive = true;

        public Lab1(String input, int filterLength) {
            this.input = input;
            this.filterLength = filterLength;
        }

        public String getSplitString() {
            return splitString;
        }

        public void setSplitString(String splitString) {
            this.splitString = splitString;
        }

        public boolean isCaseInsensitive() {
            return caseInsensitive;
        }

        public void setCaseInsensitive(boolean caseInsensitive) {
            this.caseInsensitive = caseInsensitive;
        }

        public Stream<String> getSortedWords() {
            return stream(input.split(splitString))
                    .filter(s -> !(s.length() < 1))
                    .filter(s -> s.length() == filterLength)
                    .sorted(caseInsensitive ? String.CASE_INSENSITIVE_ORDER : Comparator.naturalOrder());
        }

        public void print() {
            getSortedWords().forEach(System.out::println);
        }

        @Override
        public String toString() {
            return getSortedWords().reduce("", (s1, s2) -> s1 + "\n" + s2).trim();
        }
    }
}
