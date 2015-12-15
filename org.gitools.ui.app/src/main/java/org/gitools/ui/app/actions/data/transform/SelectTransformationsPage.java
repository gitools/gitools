package org.gitools.ui.app.actions.data.transform;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IFunctionParameter;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.transform.FoldChangeFunction;
import org.gitools.matrix.transform.LogFunction;
import org.gitools.matrix.transform.TransformFunctionFactory;
import org.gitools.matrix.transform.parameters.AbstractFunctionParameter;
import org.gitools.matrix.transform.parameters.DoubleParameter;
import org.gitools.ui.core.utils.DocumentChangeListener;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.jgoodies.binding.adapter.Bindings.bind;


public class SelectTransformationsPage extends AbstractWizardPage {

    private final MatrixLayer<Double> resultLayer;
    private JPanel rootPanel;
    private JTable transformationTable;
    private JButton removeButton;
    private JPanel editPanel;
    private JButton upButton;
    private JButton downButton;
    private JButton addButton;
    private JTextField resultLayerName;
    private JPanel addnewPanel;
    private JPanel tableButtonPanel;
    private JPanel transformationPanel;
    private JList<ConfigurableTransformFunction> transformFunctionJList;
    private JLabel transformFunctionDescription;
    private JButton cancelNewButton;
    private JButton acceptNewButton;
    private TransformationFunctionTableModel tableModel;
    private Heatmap heatmap;


    public SelectTransformationsPage(Heatmap heatmap) {
        super();
        this.heatmap = heatmap;

        resultLayer = createResultLayer();

        init();
    }

    private MatrixLayer<Double> createResultLayer() {

        MatrixLayer<Double> resultLayer = new MatrixLayer<>(getNewLayerName(), Double.class);
        return resultLayer;
    }


    public void init() {

        setTitle("Transformation selection");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));

        switchToSelectedFunctionPanel();

        tableModel = new TransformationFunctionTableModel();
        final ArrayList<ConfigurableTransformFunction> funcs = new ArrayList<>();
        funcs.add(new LogFunction());
        funcs.add(new FoldChangeFunction(resultLayer));
        tableModel.setFunctions(funcs);
        transformationTable.setModel(tableModel);
        transformationTable.getColumnModel().getColumn(0).setMaxWidth(100);
        transformationTable.getColumnModel().getColumn(0).setMinWidth(80);
        transformationTable.getColumnModel().getColumn(1).setMaxWidth(200);
        transformationTable.getColumnModel().getColumn(1).setMinWidth(150);
        transformationTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateControls();
            }
        });
        List<ConfigurableTransformFunction> selectFuncs = TransformFunctionFactory.get(resultLayer);
        final ConfigurableTransformFunction[] transformFunctions = selectFuncs.toArray(new ConfigurableTransformFunction[selectFuncs.size()]);
        /*transformationsCB.setModel(new DefaultComboBoxModel(transformFunctions));
        transformationsCB.setSelectedIndex(0);*/

        transformFunctionJList.setListData(transformFunctions);
        transformFunctionJList.setSelectedIndex(0);
        transformFunctionJList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                ConfigurableTransformFunction selectedValue = transformFunctionJList.getSelectedValue();
                String labelText = String.format("<html><div style=\"width:%dpx;\">%s", 500, selectedValue.getDescription());
                if (selectedValue.getParamterCount() > 0) {
                    labelText += "<br/> Configurable parameters:<ul>";
                    for (String key : selectedValue.getParameters().keySet()) {
                        labelText += "<li><b>" + key + ":</b> " + selectedValue.getParameters().get(key).getDescription() + "</li>";
                    }

                }
                labelText += "</div><html>";
                transformFunctionDescription.setText(labelText);
            }
        });


        PresentationModel<MatrixLayer<Double>> layerModel = new PresentationModel<>(resultLayer);
        bind(resultLayerName, layerModel.getModel(MatrixLayer.PROPERTY_ID));
        resultLayerName.setText("transform-" + UUID.randomUUID().toString().substring(0,4));
        resultLayerName.getDocument().addDocumentListener(new DocumentChangeListener() {
            @Override
            protected void update(DocumentEvent e) {
                updateControls();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = transformationTable.getSelectedRow();
                if (selectedRow == funcs.size()-1) {
                    transformationTable.clearSelection();
                    if (funcs.size() != 1) {
                        transformationTable.addRowSelectionInterval(funcs.size()-2, funcs.size()-2);
                    }
                }
                tableModel.remove(selectedRow);
                updateControls();
            }
        });

        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.moveUp(transformationTable.getSelectedRow());
                updateControls();
            }
        });

        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.moveDown(transformationTable.getSelectedRow());
                updateControls();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToAddFunctionPanel();
            }
        });

        acceptNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.add(transformFunctionJList.getSelectedValue());
                switchToSelectedFunctionPanel();
                updateControls();
            }
        });

        cancelNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchToSelectedFunctionPanel();
            }
        });

        updateControls();

    }

    private void switchToSelectedFunctionPanel() {
        addnewPanel.setEnabled(false);
        addnewPanel.setVisible(false);
        transformationPanel.setEnabled(true);
        transformationPanel.setVisible(true);
        updateControls();
    }

    private void switchToAddFunctionPanel() {
        transformationPanel.setEnabled(false);
        transformationPanel.setVisible(false);
        addnewPanel.setVisible(true);
        addnewPanel.setEnabled(true);
        setComplete(false);
    }

    @Override
    public JComponent createControls() {
        return rootPanel;
    }

    public List<ConfigurableTransformFunction> getTransformationFunctions() {
        return tableModel.getAll();
    }



    @Override
    public void updateControls() {
        super.updateControls();
        removeButton.setEnabled(transformationTable.getSelectedRowCount() == 1);
        upButton.setEnabled(transformationTable.getSelectedRowCount() == 1 && transformationTable.getSelectedRow() != 0);
        downButton.setEnabled(transformationTable.getSelectedRowCount() == 1 && transformationTable.getSelectedRow() != transformationTable.getRowCount()-1);
        keepTableUpdated();

        addParameterComponents();

        setComplete(tableModel != null && tableModel.getRowCount() > 0
                && validateFunctionParameters()
                && validLayerName());
    }

    private boolean validLayerName() {
        String newLayerName = getNewLayerName();
        if(newLayerName.equals("")) {
            setMessage("Define a layer name and choose the transformation(s)");
            return false;
        }

        for (String existingLayer : heatmap.getLayers().getIds()) {
            if (existingLayer.toLowerCase().equals(newLayerName.toLowerCase()) ||
                    newLayerName.toLowerCase().equals(heatmap.getLayers().get(existingLayer).getName().toLowerCase())) {
                setMessage(MessageStatus.ERROR, "Chosen layer name already exists");
                return false;
            }
        }
        setMessage("Choose and edit the transformation(s)");
        return true;
    }

    private void keepTableUpdated() {

        if (tableModel != null && tableModel.get() != null) {
            transformationTable.setEnabled(tableModel.get().validate());
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//                updateUI();
                transformationTable.updateUI();
            }
        });
    }


    private boolean validateFunctionParameters() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (!tableModel.get(i).validate()) {
                    return false;
            }
        }

        return true;
    }

    private void addParameterComponents() {

        if (transformationPanel == null || transformationTable.getSelectedRow() < 0) {
            return;
        }

        ConfigurableTransformFunction func = tableModel.get(transformationTable.getSelectedRow());

        if (func == null) {
            return;
        }

        editPanel.removeAll();
        editPanel.setLayout(new GridLayout(0,2));

        if (func.getParamterCount() > 0) {

            addParameterComponent(new JLabel("<html><body><b>" + func.getName() + "</b></body></html>"));
            addParameterComponent(new JLabel("Parameters"));
            for (String key : func.getParameters().keySet()) {
                IFunctionParameter param = func.getParameters().get(key);

                Component c = getParamInput((AbstractFunctionParameter) param);
                String labelText = String.format("<html><div style=\"width:%dpx;\">%s</div><html>", 300, param.getDescription());
                addParameterComponent(new JLabel(labelText));
                addParameterComponent(c);
            }

        } else {
            addParameterComponent(new JLabel("<html><body><b>" + func.getName() + "</b></body></html>"));
            addParameterComponent(new JLabel("No configurable parameters"));
        }
    }

    private Component getParamInput(AbstractFunctionParameter param) {
        Component c = null;
        if (param.hasChoices()) {
            JComboBox jComboBox = new JComboBox();
            bind(jComboBox,
                    new SelectionInList<>(param.getChoices(), new PropertyAdapter<>(param, param.PROPERTY_PARAMETER_VALUE)));


            jComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    keepTableUpdated();
                }
            });


            c = jComboBox;

        } else if (param.getParameterValue().getClass() == Integer.class) {
            JFormattedTextField textField = new JFormattedTextField();
            /*bind (textField,
                    createStringConverter(
                            new PropertyAdapter<>(param, param.PROPERTY_PARAMETER_VALUE),
                            NumberFormat.getIntegerInstance()
                    ));*/
            bind(textField, new PresentationModel<>(param).getModel(DoubleParameter.PROPERTY_PARAMETER_VALUE));


            textField.getDocument().addDocumentListener(new DocumentChangeListener() {
                @Override
                protected void update(DocumentEvent e) {
                    keepTableUpdated();
                }
            });

            c = textField;
        } else if (param.getParameterValue().getClass() == Double.class) {

            JFormattedTextField textField = new JFormattedTextField();
            bind(textField, new PresentationModel<>(param).getModel(DoubleParameter.PROPERTY_PARAMETER_VALUE));
            textField.getDocument().addDocumentListener(new DocumentChangeListener() {
                @Override
                protected void update(DocumentEvent e) {
                    keepTableUpdated();
                }
            });

            c = textField;
        }
        return c;
    }

    private void addParameterComponent(Component c) {

        if (c == null) {
            return;
        }

        editPanel.add(c);
        editPanel.revalidate();
        editPanel.repaint();
    }

    public IMatrixLayer getLayer() {
        return heatmap.getLayers().getTopLayer();
    }

    public String getNewLayerName() {
        return resultLayerName.getText();
    }

    public MatrixLayer<Double> getNewLayer() {
        String desc = "Transformed from " + getLayer().getName() + ": ";
        for (ConfigurableTransformFunction function : getTransformationFunctions()) {
            desc += function.getName() + "; ";
        }
        resultLayer.setDescription(desc);
        return resultLayer;
    }


    private class TransformationFunctionTableModel extends AbstractTableModel {
        private List<ConfigurableTransformFunction> funcs;
        private String[] columnName = {"Order", "Transformation", "Explanation"};

        @Override
        public int getRowCount() {
            return funcs.size();
        }

        @Override
        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columnName[columnIndex];
        }

        public ConfigurableTransformFunction get(int i) {
            return (ConfigurableTransformFunction) getValueAt(i, 1);
        }

        public ConfigurableTransformFunction get() {
            return (ConfigurableTransformFunction) getValueAt(transformationTable.getSelectedRow(), 1);
        }

        public List<ConfigurableTransformFunction> getAll() {
            return funcs;
        }

        public void remove(int i) {
            funcs.remove(i);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            if (rowIndex < 0 || columnIndex < 0) {
                return  null;
            }

            if (columnIndex == 0) {
                return rowIndex+1 + ". ";
            }
            else if (columnIndex == 1 && funcs.size() > 0) {
                return funcs.get(rowIndex);
            } else if (columnIndex == 2 && funcs.size() > 0) {
                return funcs.get(rowIndex).getDescription();
            }
            return null;
        }

        public void setFunctions(List<ConfigurableTransformFunction> funcs) {
            this.funcs = funcs;
        }

        public void add(ConfigurableTransformFunction func) {
            this.funcs.add(TransformFunctionFactory.createFromTemplate(func));
        }

        public void moveUp(int selectedRow) {
            int newPos = selectedRow - 1;
            ConfigurableTransformFunction f = get(selectedRow);
            remove(selectedRow);
            funcs.add(newPos, f);
            transformationTable.getSelectionModel().setSelectionInterval(newPos, newPos);
        }

        public void moveDown(int selectedRow) {
            int newPos = selectedRow + 1;
            ConfigurableTransformFunction f = get(selectedRow);
            remove(selectedRow);
            funcs.add(newPos, f);
            transformationTable.getSelectionModel().setSelectionInterval(newPos, newPos);
        }
    }
}
