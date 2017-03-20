package lab1.gui;

import com.jogamp.opengl.awt.GLCanvas;
import lab1.main.Main;

import javax.swing.*;
import java.awt.*;

public class ShowForm extends JPanel {
    private JPanel rootPanel;
    private JPanel mainPanel;
    private JPanel panelForInput;
    private JPanel radioButtonGroup;
    private JButton btnDraw;
    private JButton btnClear;
    private JRadioButton rbtnDDA;
    private JRadioButton rbtnJOGL;

    private GLCanvas canvas;
    private ButtonGroup group = new ButtonGroup();

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,
                new ShowForm(),
                "Лабораторная работа 1",
                JOptionPane.PLAIN_MESSAGE);
    }

    public ShowForm() {
        super();
        canvas = Main.createCanvas();
        mainPanel.add(canvas);
        group.add(rbtnDDA);
        group.add(rbtnJOGL);

        addListeners();

        add(rootPanel);
    }

    private void createUIComponents() {
        mainPanel = new JPanel(new GridLayout(1, 1));
    }

    private void addListeners() {
        btnDraw.addActionListener(e -> {
            if (rbtnJOGL.isSelected()) {
                ((Main) canvas.getGLEventListener(0)).setJoglDraw(true);
            } else {
                ((Main) canvas.getGLEventListener(0)).setDdaDraw(true);
            }
            canvas.repaint();
        });

        btnClear.addActionListener(e -> {
            ((Main) canvas.getGLEventListener(0)).setClear(true);
            canvas.repaint();
        });
    }
}
