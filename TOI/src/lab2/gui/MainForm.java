package lab2.gui;

import lab2.gui.task1.Task1;
import lab2.gui.task2.Task2;
import lab2.gui.task3.Task3;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JPanel {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;

    public static void main(String[] args) {
        create(null);
    }

    public static void create(Component parentComponent) {
        JOptionPane.showMessageDialog(parentComponent,
                new MainForm(),
                "Лабораторная работа 2",
                JOptionPane.PLAIN_MESSAGE);
    }

    public MainForm() {
        super();

        add(rootPanel);
        tabbedPane1.removeAll();
        tabbedPane1.addTab("Задание 1", new Task1());
        tabbedPane1.addTab("Задание 2", new Task2());
        tabbedPane1.addTab("Задание 3", new Task3());
    }
}
