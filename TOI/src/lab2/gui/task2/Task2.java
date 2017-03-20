package lab2.gui.task2;

import lab2.main.SearchAlgorithms;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;

public class Task2 extends JPanel {
    private JPanel rootPanel;
    private JButton btnLoadFromFile;
    private JTextArea textArea1;
    private JButton btnFindWord;

    protected JFileChooser fileChooser = new JFileChooser();
    protected String text;

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,
                new Task2(),
                "Задание 2",
                JOptionPane.PLAIN_MESSAGE);
    }

    public Task2() {
        super();
        add(rootPanel);

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        addListeners();
    }

    private void addListeners() {
        btnLoadFromFile.addActionListener((ActionEvent e) -> {
            fileChooser.showDialog(null, "Открыть");
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

        btnFindWord.addActionListener((ActionEvent e) -> {
            String key;
            List<Integer> indexes;

            key = JOptionPane.showInputDialog(this, "Введите искомую последовательность: ");
            indexes = SearchAlgorithms.rabinSearch(text, key);

            if (indexes != Collections.EMPTY_LIST) {
                JOptionPane.showMessageDialog(this,
                        new OutputForm(key, indexes),
                        "Результат поиска",
                        JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Искомая последовательность: [" + key + "]\n"
                                + "Искомая последовательность не найдена",
                        "Результат поиска",
                        JOptionPane.PLAIN_MESSAGE);
            }
        });
    }
}
