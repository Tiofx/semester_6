package lab1.gui;

import javax.swing.*;

public class OutputForm extends JPanel {
    private JPanel rootPanel;
    private JTextArea textArea1;

    public OutputForm() {
        super();
        add(rootPanel);
    }

    public void setText(String value) {
        textArea1.setText(value);
    }
}
