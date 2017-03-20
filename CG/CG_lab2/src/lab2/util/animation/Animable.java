package lab2.util.animation;

import lab2.util.TransformOperation;

import java.util.List;

public interface Animable<T extends TransformOperation> extends TransformOperation<T> {

    List<AbstractAnimation> getAnimations();

    default void addAnimation(AbstractAnimation simpleAnimation) {
        getAnimations().add(simpleAnimation);
    }

    default void removeAllAnimation() {
        getAnimations().removeAll(getAnimations());
    }

    default void nextFrame() {
        for (int i = 0; i < getAnimations().size(); i++) {
            if (getAnimations().get(i).hasNextFrame()) {
                transform(getAnimations().get(i).nextFrame());
            } else {
                getAnimations().remove(i);
            }
        }
    }

    default boolean hasNextFrame() {
        return getAnimations().size() > 0;
    }

}
