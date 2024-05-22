package org.example;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class TabulkaModel extends AbstractTableModel {
    private String[] columnNames = {"Název", "R", "G", "B", "Hex"};
    private java.util.List<Barva> barvy;

    public TabulkaModel() {
        barvy = new ArrayList<>();
    }

    public void pridatBarvu(Barva barva) {
        barvy.add(barva);
        fireTableDataChanged();
    }

    public Barva getBarvaAt(int rowIndex) {
        return barvy.get(rowIndex);
    }

    public java.util.List<Barva> getBarvy() {
        return barvy;
    }

    @Override
    public int getRowCount() {
        return barvy.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Barva barva = barvy.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return barva.getNazev();
            case 1:
                return barva.getR();
            case 2:
                return barva.getG();
            case 3:
                return barva.getB();
            case 4:
                return barva.getHex();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
