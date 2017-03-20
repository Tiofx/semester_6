package lab4.util;

import lab2.util.TransformOperationExtended;
import lab4.util.Drawable;
import lab4.util.VertexGetter;

import java.awt.*;


public interface Complex<T extends TransformOperationExtended> extends Drawable, TransformOperationExtended<T>, VertexGetter {
    Color getColor();
}
