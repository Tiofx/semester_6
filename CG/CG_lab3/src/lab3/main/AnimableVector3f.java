package lab3.main;

import lab2.main.figures.Vertex3f;
import lab2.util.animation.AbstractAnimation;
import lab2.util.animation.Animable;

import java.util.ArrayList;
import java.util.List;

public class AnimableVector3f extends Vertex3f implements Animable<Vertex3f> {
    protected List<AbstractAnimation> animations = new ArrayList<>();

    public AnimableVector3f() {
    }

    public AnimableVector3f(float x, float y) {
        super(x, y);
    }

    public AnimableVector3f(String name, float x, float y) {
        super(name, x, y);
    }

    public AnimableVector3f(float x, float y, float h) {
        super(x, y, h);
    }

    public AnimableVector3f(String name, float x, float y, float h) {
        super(name, x, y, h);
    }

    @Override
    synchronized public List<AbstractAnimation> getAnimations() {
        return animations;
    }
}
