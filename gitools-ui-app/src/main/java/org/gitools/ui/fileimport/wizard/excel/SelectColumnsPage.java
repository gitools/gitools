package org.gitools.ui.fileimport.wizard.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SelectColumnsPage extends AbstractWizardPage {

    private JPanel mainPanel;
    private JComboBox rowsCellsCombo;
    private JComboBox columnsCellsCombo;
    private JList valueCellsList;
    private JCheckBox hideNonNumericHeadersCheckBox;


    private ExcelReader reader;

    private DefaultComboBoxModel rowsCells;
    private DefaultComboBoxModel colsCells;
    private CheckListItem[] values;

    @Override
    public JComponent createControls() {
        return mainPanel;
    }

    @Override
    public void updateControls() {

        try {
            reader.init();

            final List<ExcelHeader> allHeaders = new ArrayList<ExcelHeader>();
            final List<ExcelHeader> numericHeaders = new ArrayList<ExcelHeader>();

            for (ExcelHeader header : reader.getHeaders()) {

                int type = header.getType();
                switch (type) {
                    case Cell.CELL_TYPE_FORMULA:
                    case Cell.CELL_TYPE_BOOLEAN:
                    case Cell.CELL_TYPE_NUMERIC:
                        numericHeaders.add(header);
                        break;
                }

                allHeaders.add(header);
            }

            if (allHeaders.size() < 2 || numericHeaders.size() < 1) {
                throw new RuntimeException("At least we need three columns (two string and one numeric column) to create a matrix");
            }

            rowsCells = new DefaultComboBoxModel(allHeaders.toArray());
            rowsCells.setSelectedItem(allHeaders.get(0));
            rowsCellsCombo.setModel(rowsCells);

            colsCells = new DefaultComboBoxModel(allHeaders.toArray());
            colsCells.setSelectedItem(allHeaders.get(1));
            columnsCellsCombo.setModel(colsCells);

            createCheckListItems(numericHeaders);

            valueCellsList.setListData(values);
            valueCellsList.setCellRenderer(new CheckListRenderer());
            valueCellsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            // Add a mouse listener to handle changing selection
            valueCellsList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent event) {
                    JList list = (JList) event.getSource();

                    int index = list.locationToIndex(event.getPoint());
                    CheckListItem item = (CheckListItem) list.getModel().getElementAt(index);

                    item.setSelected(!item.isSelected());
                    list.repaint(list.getCellBounds(index, index));
                    fireUpdated();
                }
            });

            hideNonNumericHeadersCheckBox.setEnabled(true);
            hideNonNumericHeadersCheckBox.setSelected(true);
            hideNonNumericHeadersCheckBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (hideNonNumericHeadersCheckBox.isSelected()) {
                        createCheckListItems(numericHeaders);
                    } else {
                        createCheckListItems(allHeaders);
                    }
                    valueCellsList.setListData(values);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void createCheckListItems(List<ExcelHeader> headers) {
        values = new CheckListItem[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            values[i] = new CheckListItem(headers.get(i));
        }
    }

    @Override
    public boolean isComplete() {

        if (values == null) {
            return false;
        }

        for (CheckListItem value : values) {
            if (value.isSelected()) {
                return true;
            }
        }

        return false;
    }

    public ExcelReader getReader() {
        return reader;
    }

    public void setReader(ExcelReader reader) {
        this.reader = reader;
    }

    public int getSelectedColumn() {
        ExcelHeader column = (ExcelHeader) colsCells.getSelectedItem();
        return column.getPos();
    }

    public int getSelectedRow() {
        ExcelHeader row = (ExcelHeader) rowsCells.getSelectedItem();
        return row.getPos();
    }

    public List<Integer> getSelectedValues() {

        List<Integer> valuesPos = new ArrayList<Integer>(values.length);

        for (CheckListItem v : values) {
            if (v.isSelected()) {
                valuesPos.add(v.getHeader().getPos());
            }
        }

        return valuesPos;
    }

    private class CheckListItem {
        private ExcelHeader header;
        private boolean isSelected = false;

        public CheckListItem(ExcelHeader header) {
            this.header = header;
        }

        public ExcelHeader getHeader() {
            return header;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public String toString() {
            return header.toString();
        }
    }


    private class CheckListRenderer extends JCheckBox implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {

            setEnabled(list.isEnabled());
            setSelected(((CheckListItem) value).isSelected());
            setFont(list.getFont());
            setBackground(list.getBackground());
            setForeground(list.getForeground());
            setText(value.toString());
            return this;

        }
    }
}
