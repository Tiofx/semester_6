package lab1.gui;

import lab0.IAddForm;
import lab1.Main;

import javax.swing.*;

public class Lab1AddForm extends JPanel implements IAddForm<Main.Lab1> {
    private JPanel rootPanel;
    private JTextArea textArea1;
    private JSpinner spinner1;

    public Lab1AddForm() {
        super();
        add(rootPanel);
    }

    public void setText(String text) {
        textArea1.setText(text);
        textArea1.setCaretPosition(0);
    }

    @Override
    public Main.Lab1 getRecord() {
        return new Main.Lab1(getText(), getNumber());
    }

    public String getText() {
        return textArea1.getText();
    }

    public int getNumber() {
        return (int) spinner1.getValue();
    }

    @Override
    public Boolean[] isFieldValid() {
        return new Boolean[]{canGetText(), canGetNumber()};
    }

    public boolean canGetText() {
        return true;
    }

    public boolean canGetNumber() {
        try {
            int n = getNumber();
            if (n <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
