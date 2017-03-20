package lab2.gui;

import lab2.main.figures.Quad;

import javax.swing.table.AbstractTableModel;

public final class QuadTableModel extends AbstractTableModel {
    protected Quad quad;

    public QuadTableModel(Quad quad) {
        this.quad = quad;
    }

    @Override
    public int getRowCount() {
        return 4;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return String.format("%.2f", getValue(rowIndex, columnIndex));
    }

    private float getValue(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return quad.getVertices()[rowIndex].getX();
            case 1:
                return quad.getVertices()[rowIndex].getY();
            case 2:
                return quad.getVertices()[rowIndex].getH();
            default:
                throw new ArrayIndexOutOfBoundsException(this.getClass().toString());
        }
    }
}
