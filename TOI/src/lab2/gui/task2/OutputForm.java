package lab2.gui.task2;

import javax.swing.*;
import java.util.List;

public class OutputForm extends JPanel {
    private JPanel rootPanel;
    private JList list1;
    private JLabel lblListSize;
    private JLabel lblKeyValue;

    public OutputForm(String key, List<Integer> indexes) {
        super();
        lblKeyValue.setText("[" + key + "]");
        lblListSize.setText(String.valueOf(indexes.size()));
        list1.setListData(indexes.toArray());
        add(rootPanel);
    }
}
