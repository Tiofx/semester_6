import javax.swing.*;

public class MainForm extends JPanel {
    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JButton btnLab1;
    private JButton btnLab2;
    private JButton btnLab3;
    private JButton btnLab4;
    private JTextPane цельРаботыИзучитьМетодTextPane;
    private JTextPane цельРаботыОзнакомитьсяСTextPane;
    private JTextPane цельРаботыОсвоитьИTextPane;
    private JTextPane цельРаботыИзучитьОператорыTextPane;

    public MainForm() {
        super();

        addListeners();
        add(rootPanel);
    }

    private void addListeners() {
        btnLab1.addActionListener(e -> {
            lab1.Main.create(this);
        });

        btnLab2.addActionListener(e -> {
            lab2.Main.create(this);
        });

        btnLab3.addActionListener(e -> {
            lab3.Main.create(this);
        });

        btnLab4.addActionListener(e -> {
            lab4.Main.create(this);
        });
    }

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(
                null,
                new MainForm(),
                "Технологии обработки информации",
                JOptionPane.PLAIN_MESSAGE);
    }
}
