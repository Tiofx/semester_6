package lab4.gui;

import com.jogamp.opengl.awt.GLCanvas;
import lab2.gui.Animation;
import lab2.main.figures.AnimableQuad;
import lab4.Main;
import lab4.main.*;
import lab4.util.TransformMatrix;
import lab4.util.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.util.function.Supplier;

public class ShowForm extends JPanel {
    private JPanel rootPanel;
    private JPanel showPanel;
    private JPanel inputPanel;
    private JPanel checkBoxPanel;
    private JPanel buttonPanel;
    private JButton btnStart;
    private JButton btnClear;
    private JCheckBox checkBoxFill;
    private JButton btnPause;
    private JTabbedPane tabbedPane1;
    private JSlider sliderGlobaalSpeed;
    private JLabel lblRhombus;
    private JLabel lblTrapeze;
    private JColorChooser colorChooser;
    private JSlider sliderMoveSpeed;
    private JSlider sliderAngle;
    private JSlider sliderSize;
    private JLabel lblLabyrinth;
    private JLabel lblClipBox;

    private ButtonGroup buttonGroup = new ButtonGroup();
    private ButtonGroup buttonGroup2 = new ButtonGroup();

    private Lab4Scene scene;
    private Main jogl;
    private GLCanvas canvas;

    protected final TransformMatrix transformationMatrix = new TransformMatrix();

    protected final AnimableQuad temp = new AnimableQuad();
    protected final AnimableQuad origin = new AnimableQuad();
    protected Animation animation;
    protected Animation animationVertex;
    protected Animation animationRotate;


//    protected final AnimableVector3f vertexE;

    //    private ImageIcon rhombus;
    private ImageIcon rhombus = new ImageIcon("resource/rhombus.png");
    private ImageIcon trapeze = new ImageIcon("resource/trapeze.png");
    private ImageIcon labyrinth = new ImageIcon("resource/blackHole.png");
    private ImageIcon clipBox = new ImageIcon("resource/tv.png");


    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null,
                new ShowForm(),
                "Лабораторная работа 3",
                JOptionPane.PLAIN_MESSAGE);
    }


    private void createUIComponents() {
        canvas = Main.createCanvas();
        jogl = (Main) canvas.getGLEventListener(0);
        scene = jogl.getScene();


        new Thread(() -> {
            while (true) {
                canvas.repaint();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();


        showPanel = new JPanel(new GridLayout(1, 1));
        showPanel.add(canvas);
    }


    DropTargetListener dropTargetListener;

    public ShowForm() {
        super();

        colorChooser.setColor(Color.white);

//        TransferHandler transferHandler = new TransferHandler("icon");
//        transferHandler.setDragImage(new ImageIcon("plain.jpg").getImage());

        int iconSize = 64;

        Image temp = rhombus.getImage();
        temp = temp.getScaledInstance(iconSize, iconSize, java.awt.Image.SCALE_SMOOTH);
        rhombus.setImage(temp);

        temp = trapeze.getImage();
        temp = temp.getScaledInstance(iconSize, iconSize, java.awt.Image.SCALE_SMOOTH);
        trapeze.setImage(temp);

        labyrinth.setImage(labyrinth.getImage().getScaledInstance(iconSize, iconSize, java.awt.Image.SCALE_SMOOTH));
        clipBox.setImage(clipBox.getImage().getScaledInstance(iconSize, iconSize, java.awt.Image.SCALE_SMOOTH));

        lblRhombus.setIcon(rhombus);
        lblTrapeze.setIcon(trapeze);
        lblLabyrinth.setIcon(labyrinth);
        lblClipBox.setIcon(clipBox);


        dropTargetListener = new DropTargetListener(showPanel, scene);

        Supplier<Vector2D> velocityGetter = () -> {
            int angle = sliderAngle.getValue();
            int speed = sliderMoveSpeed.getValue();

            Vector2D velocity = new Vector2D(
                    (float) Math.cos(Math.toRadians(angle)),
                    (float) Math.sin(Math.toRadians(angle)));

            velocity.multiply(speed / speedScale);

            return velocity;
        };

        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(lblRhombus.getParent(),
                DnDConstants.ACTION_COPY, new DragListener(
                        (this::createQuad), Figure.RHOMBUS, velocityGetter));

        ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(lblTrapeze.getParent(),
                DnDConstants.ACTION_COPY, new DragListener(
                        (this::createQuad), Figure.TRAPEZE, velocityGetter));

        ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(lblLabyrinth.getParent(),
                DnDConstants.ACTION_COPY, new DragListener(
                        (this::createQuad), Figure.LABYRINTH, velocityGetter));

        ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(lblClipBox.getParent(),
                DnDConstants.ACTION_COPY, new DragListener(
                        (this::createQuad), Figure.CLIP_BOX, velocityGetter));

        addListeners();
        add(rootPanel);
    }

    public enum Figure {
        RHOMBUS, TRAPEZE, CLIP_BOX, LABYRINTH
    }

    protected final Quad tempQuad = new Quad();
    protected static final float speedScale = 400f;
    protected static final float sizeScale = 1000f;

    protected Quad createQuad(Figure figure) {
        Quad resultFigure = null;

        switch (figure) {
            case RHOMBUS:
                resultFigure = new Rhombus();
                break;
            case TRAPEZE:
                resultFigure = new Trapeze();
                break;
            case LABYRINTH:
                resultFigure = new Labyrinth();
                break;
            case CLIP_BOX:
                resultFigure = new ClipBox();
                break;
        }

        transformationMatrix.scale(sliderSize.getValue() / sizeScale, sliderSize.getValue() / sizeScale,
                ((figure == Figure.LABYRINTH || figure == Figure.CLIP_BOX) ? 0.5f : 1f));

        resultFigure.setResult(tempQuad);
        resultFigure.transform(transformationMatrix);
        resultFigure.applyChange();

        resultFigure.setColor(colorChooser.getColor());

        return resultFigure;
    }

    float globalSpeedScale = 10e3f;

    private void addListeners() {
        btnStart.addActionListener(l -> {
            scene.setAnimation(true);
        });

        btnPause.addActionListener(l -> {
            scene.setAnimation(false);
        });

        btnClear.addActionListener(l -> {
            scene.clear();
        });

        sliderGlobaalSpeed.addChangeListener(l -> {
            scene.getGlobalParameters().value = sliderGlobaalSpeed.getValue() / globalSpeedScale;
        });

        checkBoxFill.addChangeListener(l -> {
            scene.getGlobalParameters().fill = checkBoxFill.isSelected();
        });
    }
}
