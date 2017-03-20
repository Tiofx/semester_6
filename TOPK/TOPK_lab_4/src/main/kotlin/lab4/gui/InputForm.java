package lab4.gui;

import lab2.main.Automation;
import lab2.util.AutomationFactory;
import lab2.util.variantNative.CodesAnalyser;
import lab2.util.variantNative.Constants;
import lab4.main.MA;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static lab4.main.MAKt.createMyAutomation;

public class InputForm extends JPanel {
    private JPanel rootPanel;
    private JButton btnLoadFromFile;
    private JTextArea txtAreaInput;
    private JTextArea txtAreaLog;
    private JTextArea txtCodes;
    private JTextArea txtLog2;
    private JTextArea txtStackLog;

    protected JFileChooser fileChooser = new JFileChooser();

    protected Automation automation = AutomationFactory.createAutomation(Constants.myVariant);
    protected CodesAnalyser automationAnalyser = new CodesAnalyser();

    protected MA ma = createMyAutomation();

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,
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
                new BufferedReader(new FileReader(fileChooser.getSelectedFile()))
                        .lines()
                        .map(s -> s + '\n')
                        .forEach(string -> txtAreaInput.setText(txtAreaInput.getText() + string));

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        txtAreaInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLabel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLabel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLabel();
            }


            protected void updateLabel() {
                automation.reset();

                Arrays.stream(txtAreaInput.getText().split("\n"))
                        .forEachOrdered(automation::sendLine);

                txtAreaLog.setText(automation.getLog().stream()
                        .map(automationAnalyser::interpret)
                        .reduce("", (s1, s2) -> s1 + "\n" + s2));

                txtAreaLog.setCaretPosition(0);

                txtCodes.setText(automation.getLog().stream()
                        .map(logContainer -> String.valueOf(logContainer.automationPosition))
                        .map(s -> "-1".equals(s) ? "\n" : s + " ")
                        .filter(s -> !s.isEmpty())
                        .reduce("", String::concat));
            }
        });

        txtCodes.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

            private void update() {
                ma.reset();

                List<Integer> result = Arrays.stream(txtCodes.getText().split("[ \n\t]"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Integer::parseInt)
                        .filter(i -> i != -1)
                        .collect(Collectors.toList());

                ma.sendAll(result);

                txtLog2.setText(lab4.main.MAUtilKt.translateResult(ma));

                txtStackLog.setText(lab4.main.MAUtilKt.translateStackLog(ma));
            }
        });
    }
}
