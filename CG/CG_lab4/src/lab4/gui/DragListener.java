package lab4.gui;

import lab4.main.Quad;
import lab4.util.Vector2D;

import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.util.function.Function;
import java.util.function.Supplier;

public class DragListener implements DragGestureListener {
    protected Function<ShowForm.Figure, Quad> quadCreator;
    protected ShowForm.Figure figure;
    protected Supplier<Vector2D> velocityGetter;

    public DragListener(Function<ShowForm.Figure, Quad> quadCreator, ShowForm.Figure figure,
                        Supplier<Vector2D> velocityGetter) {

        this.quadCreator = quadCreator;
        this.figure = figure;
        this.velocityGetter = velocityGetter;
    }

    @Override
    public void dragGestureRecognized(DragGestureEvent event) {
        Cursor cursor = null;

        Quad resultFigure = quadCreator.apply(figure);
        Vector2D velocity = velocityGetter.get();

        if (event.getDragAction() == DnDConstants.ACTION_COPY) {
            cursor = DragSource.DefaultCopyDrop;
        }

        event.startDrag(cursor, new TransferableFigure(resultFigure, velocity));
    }

}
