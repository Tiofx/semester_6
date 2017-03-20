package lab1.gui;

import lab1.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class InputForm extends JPanel {
    private JPanel rootPanel;
    private JButton btnLoadFromFile;
    private JButton btnResult;
    private Lab1AddForm lab1AddForm;

    protected JFileChooser fileChooser = new JFileChooser();
    protected String text;

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,
                new InputForm(),
                "Лабораторная работа 1",
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
            fileChooser.showDialog(null, "Открыть");
            try {
                text = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))
                        .lines()
                        .reduce("", (s1, s2) -> s1 + "\n" + s2)
                        .trim();

                lab1AddForm.setText(text);

            } catch (FileNotFoundException e1) {
                lab1AddForm.setText(e1.getMessage());
                e1.printStackTrace();
            }
        });

        btnResult.addActionListener((ActionEvent e) -> {
            Main.Lab1 lab1;
            Lab1AddForm form = lab1AddForm;
            OutputForm outputForm = new OutputForm();

            do {
                if (!form.isRecordValid()) {
                    JOptionPane.showMessageDialog(form,
                            "Не корректный ввод! Попробуйте еще раз!",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                }
            } while (!form.isRecordValid());
            lab1 = form.getRecord();

            outputForm.setText(lab1.toString());
            JOptionPane.showMessageDialog(null,
                    outputForm,
                    "Результат",
                    JOptionPane.PLAIN_MESSAGE);
        });
    }
}
