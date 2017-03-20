package lab4.gui;

import lab4.main.ClipBox;
import lab4.main.Lab4Scene;
import lab4.main.Labyrinth;
import lab4.main.Quad;
import lab4.util.TransformMatrix;
import lab4.util.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;

public class DropTargetListener extends DropTargetAdapter {
    private JPanel panel;
    private Lab4Scene scene;

    private DropTarget dropTarget;

    protected final static TransformMatrix transformMatrix = new TransformMatrix();

    public DropTargetListener(JPanel panel, Lab4Scene scene) {
        this.panel = panel;
        this.scene = scene;

        dropTarget = new DropTarget(panel, DnDConstants.ACTION_COPY,
                this, true, null);
    }

    public void drop(DropTargetDropEvent event) {
        try {

            Transferable tr = event.getTransferable();

            Quad quad = (Quad) tr.getTransferData(TransferableFigure.figureFlavor);
            Vector2D velocity = (Vector2D) tr.getTransferData(TransferableFigure.velocityFlavor);

            setTransform(event.getLocation());
            quad.transform(transformMatrix);
            quad.applyChange();

            if (event.isDataFlavorSupported(TransferableFigure.figureFlavor)) {
                event.acceptDrop(DnDConstants.ACTION_COPY);

                if (quad instanceof ClipBox) {
                    scene.addClipBox((ClipBox) quad);
                } else if (quad instanceof Labyrinth) {
                    scene.addLabyrinth((Labyrinth) quad);
                } else {
                    scene.addQuad(quad, velocity);
                }

                event.dropComplete(true);
                return;
            }
            event.rejectDrop();
        } catch (Exception e) {
            e.printStackTrace();
            event.rejectDrop();
        }
    }

    private void setTransform(Point location) {
        float x, y;

        x = 2 * location.x / (float) panel.getWidth();
        y = 2 * (1 - location.y / (float) panel.getHeight());

        x -= 1;
        y -= 1;

        transformMatrix.translate(x, y);
    }


}
