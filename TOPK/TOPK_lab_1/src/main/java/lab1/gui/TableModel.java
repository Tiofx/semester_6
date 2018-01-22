package lab1.gui;

import lab1.main.FiniteStateAutomatonInterface;

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class TableModel extends DefaultTableModel {
    protected FiniteStateAutomatonInterface automaton;

    public void setDataVector(Vector dataVector) {
        this.dataVector = dataVector;
        fireTableStructureChanged();
    }

    public void setAutomaton(FiniteStateAutomatonInterface automaton) {
        this.automaton = automaton;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column != 1) {
            return dataVector.get(row);
        } else {
            return "[ RESULT: " + automaton.check(dataVector.get(row).toString()).toString().toLowerCase() + " ]";
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
