package lab6.gui;

import com.jogamp.opengl.awt.GLCanvas;
import lab6.main.Main;

import javax.swing.*;
import java.awt.*;

public class ShowForm extends JPanel {
    private JPanel rootPanel;
    private JPanel showPanel1;
    private JSpinner spinnerIterationNumber;

    private Main jogl;
    private GLCanvas canvas;

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,
                new ShowForm(),
                "Лабораторная работа 6",
                JOptionPane.PLAIN_MESSAGE);
    }

    private void createUIComponents() {
        canvas = Main.createCanvas();
        jogl = (Main) canvas.getGLEventListener(0);


        showPanel1 = new JPanel(new GridLayout(1, 1));
        showPanel1.add(canvas);
    }

    public ShowForm() {
        super();

        addListeners();
        add(rootPanel);
    }

    private void addListeners() {
        spinnerIterationNumber.addChangeListener(l -> {
            int value = (int) spinnerIterationNumber.getValue();

            if (value > 0) {
                jogl.getLevyCurve().setIterationNumber(value);
                canvas.repaint();
            }
        });
    }
}
