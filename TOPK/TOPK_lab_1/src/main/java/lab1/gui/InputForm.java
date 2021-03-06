package lab1.gui;

import lab1.UtilKt;
import lab1.main.RegexAdapter;
import lab1.main.AutomatonInterface;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

public class InputForm extends JPanel {
    private JPanel rootPanel;
    private JButton btnLoadFromFile;
    private JTextField txtInput;
    private JLabel lblResult;
    private JButton btnAdd;
    private JTable tableData;
    private JButton btnClear;
    private JTextField regex;

    protected final TableModel tableModel = new TableModel();
    protected JFileChooser fileChooser = new JFileChooser();

    protected AutomatonInterface automaton = new RegexAdapter(UtilKt.regexForLab1());

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,
                new InputForm(),
                "Лабораторная работа 1",
                JOptionPane.PLAIN_MESSAGE);
    }

    public InputForm() {
        super();
        add(rootPanel);

        tableModel.setColumnIdentifiers(new String[]{"Выражение", "Результат"});
        tableModel.setAutomaton(automaton);
        tableModel.setDataVector(new Vector<String>(10));
        tableData.setModel(tableModel);

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        addListeners();

        txtInput.setText("a");
        txtInput.setText("");
        regex.setText(UtilKt.regexForLab1());
    }

    private void addListeners() {
        btnLoadFromFile.addActionListener((ActionEvent e) -> {
            fileChooser.showDialog(null, "Загрузить");
            try {
                new BufferedReader(new FileReader(fileChooser.getSelectedFile()))
                        .lines()
                        .forEach(string -> tableModel.getDataVector().add(string));

                tableModel.fireTableDataChanged();

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        });

        btnAdd.addActionListener(l -> {
            tableModel.getDataVector().add(txtInput.getText());
            tableModel.fireTableDataChanged();
        });

        btnClear.addActionListener(l -> {
            tableModel.getDataVector().clear();
            tableModel.fireTableDataChanged();
        });

        txtInput.getDocument().addDocumentListener(new DocumentListener() {
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

        });

        regex.getDocument().addDocumentListener(new DocumentListener() {
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
                ((RegexAdapter) automaton).changeRegex(regex.getText());
                InputForm.this.updateLabel();
            }
        });
    }

    protected void updateLabel() {
        lblResult.setText(automaton.check(txtInput.getText()).toString().toLowerCase());

        switch (automaton.check(txtInput.getText())) {
            case RIGHT:
                lblResult.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
                lblResult.setForeground(Color.GREEN.darker());
                break;
            case WRONG:
                lblResult.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
                lblResult.setForeground(Color.RED.darker());
                break;
            case ONGOING:
                lblResult.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
                lblResult.setForeground(Color.YELLOW.darker());
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
