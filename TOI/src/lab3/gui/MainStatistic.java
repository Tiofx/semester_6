package lab3.gui;

import lab3.main.WordStatistic;

import javax.swing.*;
import java.util.Map;

public class MainStatistic extends JPanel {
    private JPanel rootPanel;
    private JLabel lblSum;
    private JLabel lblConstantZipf;
    private JLabel lblMaxRang;

    public MainStatistic(int sum, int maxRang, float zipfConstant) {
        super();

        lblSum.setText(String.valueOf(sum));
        lblConstantZipf.setText(String.valueOf(zipfConstant));
        lblMaxRang.setText(String.valueOf(maxRang));
        add(rootPanel);
    }

    public static MainStatistic create(Map<String, WordStatistic> allDate) {
        int sum = allDate.values().stream()
                .mapToInt(WordStatistic::getNumber)
                .sum();

        int maxRang = allDate.values().stream()
                .mapToInt(WordStatistic::getRang)
                .max()
                .getAsInt();

        float zipfConstn = (float) allDate.values().stream()
                .mapToDouble(wordStatistic -> wordStatistic.getRang() * wordStatistic.getFrequency())
                .average()
                .getAsDouble();

        return new MainStatistic(sum, maxRang, zipfConstn);
    }
}
