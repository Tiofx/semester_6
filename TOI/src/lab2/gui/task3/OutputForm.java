package lab2.gui.task3;

import javax.swing.*;

public class OutputForm extends JPanel {
    private JPanel rootPanel;
    private JScrollPane scrollPane;
    private JTable table1;

    public OutputForm() {
        super();

        add(rootPanel);
    }

    public OutputForm(JTable table) {
        super();

        table1.setModel(table.getModel());
        scrollPane.add(table);
        add(rootPanel);
    }

    public void setTable(JTable table) {
        table1.setModel(table.getModel());
        scrollPane.add(table);
    }
}
