package lab3.gui;

import com.jogamp.opengl.awt.GLCanvas;
import lab2.gui.Animation;
import lab2.main.figures.AnimableQuad;
import lab2.util.animation.SimpleAnimation;
import lab3.main.AnimableVector3f;
import lab3.main.Lab3Scene;
import lab3.main.Main;
import lab3.main.TransformMatrix;

import javax.swing.*;
import java.awt.*;

public class ShowForm extends JPanel {
    private JPanel rootPanel;
    private JPanel showPanel;
    private JPanel inputPanel;
    private JPanel checkBoxPanel;
    private JCheckBox checkBoxGrid;
    private JCheckBox checkBoxLabels;
    private JPanel buttonPanel;
    private JButton btnRotate;
    private JButton btnOrigin;
    private JSlider sliderAxisScale;
    private JPanel sliderPanel;
    private JSlider sliderFpsRate;
    private JSlider sliderTotalTime;
    private JPanel panelForAnimation;
    private JCheckBox checkBoxFill;
    private JPanel panelForSteps;
    private JButton btnStepOne;
    private JButton btnStop;
    private JPanel panelForMovement;
    private JSlider sliderMoveX;
    private JSlider sliderMoveY;
    private JSpinner spinnerCoordinateX;
    private JTextField txtRotateAngle;
    private JSpinner spinnerCoordinateY;
    private JRadioButton rbtnCounterclockWise;
    private JRadioButton rbtnClockWise;
    private JTabbedPane tabbedPane1;
    private JCheckBox checkBoxAnimation;
    private JButton btnStepTwo;
    private JButton btnStepThree;
    private JButton btnMove;
    private JSlider sliderRotateSpeed;
    private JRadioButton rbtnAroundOrigin;
    private JRadioButton rbtnAroundE;

    private ButtonGroup buttonGroup = new ButtonGroup();
    private ButtonGroup buttonGroup2 = new ButtonGroup();

    private Lab3Scene scene;
    private Main jogl;
    private GLCanvas canvas;

    protected final TransformMatrix transformationMatrix = new TransformMatrix();

    protected final AnimableQuad temp = new AnimableQuad();
    protected final AnimableQuad origin = new AnimableQuad();
    protected Animation animation;
    protected Animation animationVertex;
    protected Animation animationRotate;


    protected final AnimableVector3f vertexE;


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

        showPanel = new JPanel(new GridLayout(1, 1));
        showPanel.add(canvas);
    }

    public ShowForm() {
        super();

        setStepsEnable(false);
        btnStepOne.setEnabled(true);

        vertexE = scene.getVertexE();

        buttonGroup.add(rbtnClockWise);
        buttonGroup.add(rbtnCounterclockWise);

        buttonGroup2.add(rbtnAroundOrigin);
        buttonGroup2.add(rbtnAroundE);

        transformationMatrix.setIdentity();
        scene.getBase().copyTo(origin);
        origin.setResult(scene.getBase());

        animation = new Animation(transformationMatrix, scene.getBase(),
                () -> canvas.repaint());

        animationVertex = new Animation(transformationMatrix, vertexE,
                () -> canvas.repaint());

        animationRotate = new RotateAnimation(transformationMatrix, scene.getResult(),
                () -> canvas.repaint());

        addListeners();
        add(rootPanel);
    }

    private int oldX, oldY;


    protected float getAngle() {
        String rawValue = txtRotateAngle.getText();
        float value;

        try {
            value = Float.parseFloat(rawValue);
            value *= rbtnClockWise.isSelected() ? -1 : 1;
            return value;
        } catch (Exception e) {
            return 0;
        }
    }

    private void rotateQuad() {
        if (checkBoxAnimation.isSelected() && sliderRotateSpeed.getValue() != 0) {
            // TODO: 21/02/2017 comment out
//            btnRotate.setEnabled(false);
            //btnMove.setEnabled(false);

            if (rbtnAroundOrigin.isSelected()) {
                scene.getResult().addAnimation(new lab3.main.RotateAnimation(getAngle()));
            } else {
                scene.getResult().addAnimation(new lab3.main.RotateAroundAnimation(getAngle(), vertexE));
            }

            animationRotate.setFpsRate(sliderRotateSpeed.getValue());
            animationRotate.setTotalTime((getAngle() + 1) * 1000 / sliderRotateSpeed.getValue());

            Thread work = new Thread(animationRotate);
            work.start();

            new Thread(() -> {
                while (work.isAlive()) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                scene.getBase().applyChange();
                btnRotate.setEnabled(true);
                //btnMove.setEnabled(true);
            }).start();

        } else {
            if (rbtnAroundOrigin.isSelected()) {
                transformationMatrix.rotateByDegrees(getAngle());
            } else {
                transformationMatrix.rotateAroundByDegrees(getAngle(), vertexE.x, vertexE.y);
            }
            scene.getResult().transform(transformationMatrix);
            scene.getResult().applyChange();

            canvas.repaint();
        }
    }

    private void moveQuad() {
        moveQuad(false);
    }

    private void moveQuad(boolean moveVertexE) {
        if (checkBoxAnimation.isSelected() && moveVertexE) {
//            btnRotate.setEnabled(false);
            //btnMove.setEnabled(false);

            scene.getBase().addAnimation(new SimpleAnimation(transformationMatrix, frameCount()));
            vertexE.addAnimation(new SimpleAnimation(transformationMatrix, frameCount()));

            animation.setFpsRate(sliderFpsRate.getValue());
            animation.setTotalTime(sliderTotalTime.getValue());

            if (moveVertexE) {
                new Thread(animationVertex).start();
            }

            Thread work = new Thread(animation);
            work.start();


            new Thread(() -> {
                while (work.isAlive()) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                scene.getBase().applyChange();
                vertexE.applyChange();
                btnRotate.setEnabled(true);
                //btnMove.setEnabled(true);
            }).start();

        } else {
            scene.getBase().applyChange();
            scene.getResult().transform(transformationMatrix);
            scene.getResult().applyChange();

            if (moveVertexE) {
                vertexE.transform(transformationMatrix);
                vertexE.applyChange();
            }

            canvas.repaint();
        }
    }

    private void addListeners() {
        mainListeners();
        mainParametersListeners();
        rotateListeners();
        settingListeners();
    }

    private void mainListeners() {
        btnRotate.addActionListener(l -> {
            rotateQuad();
        });


        btnStop.addActionListener(l -> {
            vertexE.removeAllAnimation();
            scene.getBase().removeAllAnimation();
            scene.getResult().removeAllAnimation();
        });

        btnOrigin.addActionListener(l -> {
            sliderMoveX.setValue(0);
            sliderMoveY.setValue(0);
            transformationMatrix.setIdentity();
            scene.getBase().removeAllAnimation();
            scene.getResult().removeAllAnimation();

            vertexE.x = Float.parseFloat(spinnerCoordinateX.getValue().toString());
            vertexE.y = Float.parseFloat(spinnerCoordinateY.getValue().toString());
            vertexE.redoChange();

            origin.redoChange();
            origin.getResultMatrix().redoChange();
            origin.getResultMatrix().getResultMatrix().redoChange();
//            scene.transform(transformationMatrix);
            canvas.repaint();
        });
    }

    private void mainParametersListeners() {
        sliderAxisScale.addChangeListener(l -> {
            jogl.setCoordinateSize(sliderAxisScale.getValue() + 1);
            canvas.repaint();
        });

        sliderMoveX.addChangeListener(l -> {
            transformationMatrix.translate(sliderMoveX.getValue() - oldX, sliderMoveY.getValue() - oldY);
            oldX = sliderMoveX.getValue();
            oldY = sliderMoveY.getValue();

            moveQuad();
        });

        sliderMoveY.addChangeListener(l -> {
            transformationMatrix.translate(sliderMoveX.getValue() - oldX, sliderMoveY.getValue() - oldY);
            oldX = sliderMoveX.getValue();
            oldY = sliderMoveY.getValue();

            moveQuad();
        });
    }

    private int x, y;

    private void rotateListeners() {
        spinnerCoordinateX.addChangeListener(l -> {
            vertexE.x = Float.parseFloat(spinnerCoordinateX.getValue().toString());
            vertexE.redoChange();
            canvas.repaint();
        });

        spinnerCoordinateY.addChangeListener(l -> {
            vertexE.y = Float.parseFloat(spinnerCoordinateY.getValue().toString());
            vertexE.redoChange();
            canvas.repaint();
        });


        btnStepOne.addActionListener(l -> {

            x = (int) vertexE.x;
            y = (int) vertexE.y;
            transformationMatrix.translate(-x, -y);

            moveQuad(true);

            btnStepOne.setEnabled(false);
            btnStepTwo.setEnabled(true);
        });

        btnStepTwo.addActionListener(l -> {
            rotateQuad();
            btnStepThree.setEnabled(true);
        });

        btnStepThree.addActionListener(l -> {

            scene.getBase().applyChange();

            transformationMatrix.translate(x, y);

            moveQuad(true);

            btnStepOne.setEnabled(true);
            btnStepTwo.setEnabled(false);
            btnStepThree.setEnabled(false);
        });

        rbtnAroundE.addChangeListener(l -> {
            if (rbtnAroundE.isSelected()) {
                setStepsEnable(false);
                btnStepOne.setEnabled(true);
            } else {
                setStepsEnable(false);
            }
        });
    }

    private void settingListeners() {
        checkBoxFill.addChangeListener(l -> {
            scene.getResult().setFill(checkBoxFill.isSelected());
            canvas.repaint();
        });

        checkBoxGrid.addChangeListener(l -> {
            scene.getAxis().setGridOn(checkBoxGrid.isSelected());
            canvas.repaint();
        });

        checkBoxLabels.addChangeListener(l -> {
            scene.getResult().setLabelDisplay(checkBoxLabels.isSelected());
            canvas.repaint();
        });
    }

    private void setStepsEnable(boolean enable) {
        btnStepOne.setEnabled(enable);
        btnStepTwo.setEnabled(enable);
        btnStepThree.setEnabled(enable);
    }

    protected int frameCount() {
        return frameCount(sliderFpsRate.getValue(), sliderTotalTime.getValue());
    }

    protected int frameCount(int fpsRate, int totalTime) {
        return fpsRate * totalTime / 1000;
    }
}
