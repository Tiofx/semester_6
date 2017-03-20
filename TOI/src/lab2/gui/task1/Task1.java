package lab2.gui.task1;

import lab2.main.SearchAlgorithms;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Task1 extends JPanel {
    private JPanel rootPanel;
    private JButton btnGenerateArray;
    private JTextArea textArea1;
    private JButton btnFindNumber;
    private JButton btnSettingsChange;

    protected lab2.main.Task1 task1 = new lab2.main.Task1();

    public static void main(String[] args) {
        Task1 task1 = new Task1();
        JOptionPane.showMessageDialog(null, task1);
    }

    public Task1() {
        super();
        add(rootPanel);

        addListeners();
    }

    private void addListeners() {
        btnGenerateArray.addActionListener((ActionEvent e) -> {
            task1.generateArray();
            textArea1.setText(task1.toString());
        });

        btnFindNumber.addActionListener((ActionEvent e) -> {
            int key, index;
            key = Integer.parseInt(JOptionPane.showInputDialog(this, "Введите искомое число: "));
            index = SearchAlgorithms.interpolationSearch(task1.getMas(), key);

            JOptionPane.showMessageDialog(this,
                    "Искомое число: " + key + "\n" +
                            (index != -1
                                    ? "Найденное число: " + task1.getMas()[index] + "\n" +
                                    "Индекс найденного числа: " + index
                                    : "Искомое число не найдено"));
        });

        btnSettingsChange.addActionListener((ActionEvent e) -> {
            Task1AddForm addForm = new Task1AddForm();
            addForm.setRecord(task1);

            do {
                int res = JOptionPane.showConfirmDialog(this,
                        addForm,
                        "Изменение настроек",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (res == JOptionPane.OK_OPTION) {
                    if (addForm.isRecordValid()) {
                        addForm.changeRecord();
                    } else JOptionPane.showMessageDialog(this,
                            "Входные данные не корректны! Попробуйте еще раз!",
                            "Ошибка",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    break;
                }
            } while (!addForm.isRecordValid());

        });
    }
}
