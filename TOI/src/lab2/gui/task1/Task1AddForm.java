package lab2.gui.task1;

import lab2.main.Task1;

import javax.swing.*;

public class Task1AddForm extends JPanel implements lab0.IAddForm<lab2.main.Task1> {
    private JPanel rootPanel;
    private JSpinner spinner2;
    private JSpinner spinner1;

    protected Task1 task1;

    public Task1AddForm() {
        super();
        add(rootPanel);
    }

    @Override
    public Task1 getRecord() {
        return new Task1(getNumber(), getMaxElement());
    }

    public void setRecord(Task1 task1) {
        this.task1 = task1;
        spinner1.setValue(task1.getN());
        spinner2.setValue(task1.getMaxValue());
    }

    public void changeRecord() {
        task1.setMaxValue(getMaxElement());
        task1.setN(getNumber());
    }

    public int getNumber() {
        return (int) spinner1.getValue();
    }

    public int getMaxElement() {
        return (int) spinner2.getValue();
    }

    @Override
    public Boolean[] isFieldValid() {
        return new Boolean[]{canGetNumber(), canGetMaxElement()};
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

    public boolean canGetMaxElement() {
        try {
            int n = getMaxElement();
            if (n <= 0) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
