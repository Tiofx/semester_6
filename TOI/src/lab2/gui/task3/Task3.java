package lab2.gui.task3;

import lab0.MapToObjects;
import lab2.main.GlossaryMap;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

public class Task3 extends JPanel {
    private JPanel rootPanel;
    private JButton btnloadFromFile;
    private JTextArea textArea1;
    private JButton btnGetGlossaryFile;

    protected JFileChooser fileChooser = new JFileChooser();
    protected String[] text;
    protected GlossaryMap res = new GlossaryMap();

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,
                new Task3(),
                "Задание 3",
                JOptionPane.PLAIN_MESSAGE);
    }

    public Task3() {
        super();
        add(rootPanel);

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        addListeners();
    }

    private void addListeners() {
        btnloadFromFile.addActionListener((ActionEvent e) -> {
            fileChooser.showDialog(null, "Загрузить");
            try {
                text = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))
                        .lines()
                        .toArray(String[]::new);

                textArea1.setText(Arrays.stream(text)
                        .reduce("", (s1, s2) -> s1 + "\n" + s2)
                        .trim());

                textArea1.setCaretPosition(0);
            } catch (FileNotFoundException e1) {
                textArea1.setText(e1.getMessage());
                e1.printStackTrace();
            }

        });

        btnGetGlossaryFile.addActionListener((ActionEvent e) -> {
            res.clear();

            text = textArea1.getText().split("\n");

            for (int i = 0; i < text.length; i++) {
                String line = text[i];
                final int index = i;

                Arrays.stream(line.split("[^\\wА-Яа-я]|\\d"))
                        .filter(s -> s.length() > 0)
                        .map(String::toLowerCase)
                        .forEach(s -> res.put(s, index));
            }

            OutputForm form = new OutputForm();
            form.setTable(new JTable(MapToObjects.toObj(res), new Object[]{"Слово", "Список ссылок на строки"}));

            JOptionPane.showMessageDialog(this,
                    form,
                    "Файл-глоссарий",
                    JOptionPane.PLAIN_MESSAGE);
        });
    }
}
