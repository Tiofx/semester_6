package lab4.gui;

import lab2.main.SplitScale;
import lab3.gui.OutputForm;
import lab3.main.WordStatistic;
import lab4.main.AbstractGetter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class InputForm extends JPanel {
    private JPanel rootPanel;
    private JButton btnLoadFromFile;
    private JTextArea textArea1;
    private JButton btnAbstracting;

    protected JFileChooser fileChooser = new JFileChooser();
    protected String text;

    public static void main(String[] args) {
        create(null);
    }

    public static void create(Component parentComponent) {
        JOptionPane.showMessageDialog(parentComponent,
                new InputForm(),
                "Лабораторная работа 4",
                JOptionPane.PLAIN_MESSAGE);
    }

    public InputForm() {
        super();
        add(rootPanel);

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        addListeners();
    }

    private void addListeners() {
        btnLoadFromFile.addActionListener((ActionEvent e) -> {
            fileChooser.showDialog(null, "Загрузить");
            try {
                text = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))
                        .lines()
                        .reduce("", (s1, s2) -> s1 + "\n" + s2)
                        .trim();

                textArea1.setText(text);
                textArea1.setCaretPosition(0);
            } catch (FileNotFoundException e1) {
                textArea1.setText(e1.getMessage());
                e1.printStackTrace();
            }
        });

        btnAbstracting.addActionListener((ActionEvent event) -> {
            Map<String, Long> finalMap = new LinkedHashMap<>();
            Map<String, WordStatistic> allData = new LinkedHashMap<>();
            text = textArea1.getText();

            Arrays.stream(text.split(SplitScale.BY_WORDS.getSplitRegex()))
                    .filter(s -> s.length() > 0)
                    .filter(s -> s.length() > 3)
                    .collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Long> comparingByValue().reversed())
                    .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));

            long sum = finalMap.entrySet().stream()
                    .mapToLong(Map.Entry::getValue)
                    .sum();

            int index = 1;

            for (String key : finalMap.keySet()) {
                WordStatistic stat =
                        new WordStatistic(index, Math.toIntExact(finalMap.get(key)), (float) finalMap.get(key) / sum);
                allData.put(key, stat);
                index++;
            }

            String temp =
                    JOptionPane.showInputDialog(this,
                            "Введите количество предложений необходимых для реферата: ",
                            "Размер реферата",
                            JOptionPane.QUESTION_MESSAGE);

            int size = Integer.parseInt(temp);

            JOptionPane.showMessageDialog(this,
                    new lab4.gui.OutputForm(OutputForm.create(allData), text, AbstractGetter.getAbstract(text, allData, size)),
                    "Анализ и реферирование текста на основе законов Зипфа",
                    JOptionPane.PLAIN_MESSAGE);
        });
    }
}
