package lab5.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import lab2.main.figures.Axis;
import lab2.util.Scene;

public class Lab5Scene extends Scene {
    protected float coordinateSize = 11;
    protected Axis axis = new Axis();

    protected Function function1 = new Function(10, 1);
    protected AngleFunction function2 = new AngleFunction(3, 1, 1);

    protected float speed = 1f;
    protected boolean animation = false;
    protected boolean firstFunctionSelected = true;

    protected final static float baseDx = 0.05f;
    protected final static float baseDAngle = 0.02f;

    public Lab5Scene() {
        function1.setxBegin(-10f);
        function1.setxEnd(10f);
        function1.setDx(baseDx * speed);

        function2.setBeginAngle(0);
        function2.setdAngle(baseDAngle);
    }

    public Function getFunction1() {
        return function1;
    }

    public AngleFunction getFunction2() {
        return function2;
    }

    public void setCoordinateSize(float coordinateSize) {
        this.coordinateSize = coordinateSize;
    }

    public void setFirstFunctionSelected(boolean firstFunctionSelected) {
        this.firstFunctionSelected = firstFunctionSelected;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        function1.setDx(baseDx * speed);
        function2.setdAngle(baseDAngle * speed);
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;

        if (animation) {
            if (firstFunctionSelected) {
                if (!function1.hasNext()) {
                    function1.reset();
                }
            } else {
                if (!function2.hasNext()) {
                    function2.reset();
                }
            }
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL2 gl = drawable.getGL().getGL2();

        if (animation) {
            if (firstFunctionSelected) {
                function1.nextFrame();
            } else {
                function2.nextFrame();
            }
        }

        if (firstFunctionSelected) {
            function1.display(drawable, coordinateSize);
        } else {
            function2.display(drawable, coordinateSize);
        }

        axis.setCoordinateSize(coordinateSize);
        axis.display(drawable);
    }

    public void reset() {
        animation = false;
        if (firstFunctionSelected) {
            function1.reset();
        } else {
            function2.reset();
        }
    }


}
