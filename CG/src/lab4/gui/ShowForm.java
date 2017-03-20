package lab4.gui;

import javax.swing.*;
import java.awt.*;

import static javax.swing.SwingConstants.CENTER;

public class ShowForm extends JPanel {
    private JPanel rootPanel;
    private JButton btnLab1;
    private JButton btnLab2;
    private JButton btnLab3;
    private JButton btnLab4;
    private JButton btnLab5;
    private JButton btnLab6;
    private JPanel panelHead;
    private JPanel back;
    private JLabel lbl;

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,
                new ShowForm(),
                "Лабороторные работы по КГ",
                JOptionPane.PLAIN_MESSAGE);
    }

    public ShowForm() {
        super();
//        System.setProperty("sun.java2d.d3d", "false");
        add(rootPanel);
        addListeners();
    }

    private void addListeners() {
        btnLab1.addActionListener(l -> {
            JOptionPane.showMessageDialog(this,
                    new lab1.gui.ShowForm(),
                    "Лабораторная работа №1",
                    JOptionPane.PLAIN_MESSAGE);
        });

        btnLab2.addActionListener(l -> {
            JOptionPane.showMessageDialog(this,
                    new lab2.gui.ShowForm(),
                    "Лабораторная работа №2",
                    JOptionPane.PLAIN_MESSAGE);
        });


        btnLab3.addActionListener(l -> {
            lab3.gui.ShowForm showForm = new lab3.gui.ShowForm();

            JOptionPane.showMessageDialog(this,
//                    new lab3.lab4.lab4.gui.ShowForm(),
                    showForm,
                    "Лабораторная работа №3",
                    JOptionPane.PLAIN_MESSAGE);

            showForm.setVisible(false);
            showForm.removeAll();
        });

        btnLab4.addActionListener(l -> {
            JOptionPane.showMessageDialog(this,
//                    new lab4.lab4.lab4.gui.ShowForm(),
                    null,
                    "Лабораторная работа №4",
                    JOptionPane.PLAIN_MESSAGE);
        });

        btnLab5.addActionListener(l -> {
            JOptionPane.showMessageDialog(this,
//                    new lab5.lab4.lab4.gui.ShowForm(),
                    null,
                    "Лабораторная работа №5",
                    JOptionPane.PLAIN_MESSAGE);
        });

        btnLab6.addActionListener(l -> {
            JOptionPane.showMessageDialog(this,
//                    new lab1.lab4.lab4.gui.ShowForm(),
                    null,
                    "Лабораторная работа №6",
                    JOptionPane.PLAIN_MESSAGE);
        });
    }

    private void createUIComponents() {
        back = new BackgroundPanel();
        back.setLayout(new BorderLayout());
        lbl = new JLabel("",
                new ImageIcon("/Users/andrej/IdeaProjects/semester_6/CG/resource/jogl_j3d_logo.png"), CENTER);
    }

}
