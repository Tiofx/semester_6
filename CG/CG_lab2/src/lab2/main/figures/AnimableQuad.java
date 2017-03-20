package lab2.main.figures;

import lab2.util.animation.AbstractAnimation;
import lab2.util.animation.Animable;

import java.util.ArrayList;
import java.util.List;

public class AnimableQuad extends Quad implements Animable<Quad> {
    protected List<AbstractAnimation> animations = new ArrayList<>();

    public AnimableQuad() {
    }

    public AnimableQuad(Vertex3f[] vertices) {
        super(vertices);
    }

    @Override
    synchronized public List<AbstractAnimation> getAnimations() {
        return animations;
    }
}
