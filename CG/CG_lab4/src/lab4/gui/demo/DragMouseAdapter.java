package lab4.gui.demo;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DragMouseAdapter extends MouseAdapter {

    public void mousePressed(MouseEvent e) {
        JComponent c = (JComponent) e.getSource();

//        dragListener.dragGestureRecognized(new DragGestureEvent());
        TransferHandler handler = c.getTransferHandler();

        handler.exportAsDrag(c, e, TransferHandler.COPY);
    }
}
