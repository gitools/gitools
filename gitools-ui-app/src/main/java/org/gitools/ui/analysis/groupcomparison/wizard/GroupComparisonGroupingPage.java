package org.gitools.ui.analysis.groupcomparison.wizard;

import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringResults;
import org.gitools.analysis.clustering.method.annotations.AnnPatClusteringData;
import org.gitools.analysis.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroup;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupAnnotation;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupEnum;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupValue;
import org.gitools.analysis.groupcomparison.filters.GroupByLabelPredicate;
import org.gitools.analysis.groupcomparison.filters.GroupByValuePredicate;
import org.gitools.analysis.stats.mtc.MTC;
import org.gitools.analysis.stats.test.Test;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.matrix.filter.DataIntegrationCriteria;
import org.gitools.core.matrix.filter.PatternFunction;
import org.gitools.ui.IconNames;
import org.gitools.ui.heatmap.panel.settings.headers.SpinnerCellEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.wizard.add.data.DataIntegrationCriteriaDialog;
import org.gitools.ui.wizard.common.PatternSourcePage;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.operators.Operator;
import org.gitools.utils.progressmonitor.DefaultProgressMonitor;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class GroupComparisonGroupingPage extends AbstractWizardPage {


    private JPanel panel1;
    private JComboBox layerCb;
    private JComboBox dimensionCb;
    private JButton addButton;
    private JButton removeButton;
    private JTable groupsTable;
    private JLabel dataLabel;
    private JButton mergeButton;
    private JButton splitButton;
    private JRadioButton annotationRadioButton;
    private JRadioButton valueRadioButton;
    private JRadioButton noConstraintRadioButton;


    private DimensionGroupTableModel tableModel = new DimensionGroupTableModel();
    private List<DimensionGroup> removedItems = new ArrayList<DimensionGroup>();
    private Heatmap heatmap;
    private DimensionGroupEnum groupingType;


    public GroupComparisonGroupingPage(Heatmap heatmap, DimensionGroupEnum groupingType) {
        super();

        this.heatmap = heatmap;
        this.groupingType = groupingType;

        setTitle("Select data and statistical test");

        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));

        layerCb.setModel(new DefaultComboBoxModel(heatmap.getLayers().getIds()));


        groupsTable.setModel(tableModel);

        setTitle("Groups");
        setMessage("Add / Remove groups and merge by selecting the same group number.");

        TableColumnModel columnModel = groupsTable.getColumnModel();
        columnModel.getColumn(2).setPreferredWidth(50);
        columnModel.getColumn(2).setCellEditor(new SpinnerCellEditor(new SpinnerNumberModel()));
        columnModel.getColumn(2).getCellEditor().addCellEditorListener(new CellEditorListener() {
            @Override
            public void editingStopped(ChangeEvent e) {
                tableModel.fireTableDataChanged();
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                tableModel.fireTableDataChanged();
            }
        });
        groupsTable.setRowHeight(20);

        groupsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateButtons();
            }
        });


        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelected();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getSelectedGroupingType().equals(DimensionGroupEnum.Annotation)) {
                    //TODO: create Dialog with removedItems
                } else if (getSelectedGroupingType().equals(DimensionGroupEnum.Free)) {
                    //TODO: create Dialog with paste labels
                } else if (getSelectedGroupingType().equals(DimensionGroupEnum.Value)) {
                    createValueGroup();
                }
            }
        });

        dimensionCb.setModel(new DefaultComboBoxModel(new String[]{"Columns", "Rows"}));

        setComplete(true);

        mergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performMerge();
            }
        });
        splitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSplit();
            }
        });
        dimensionCb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initGroups();
            }
        });

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initGroups();
            }
        };
        annotationRadioButton.addActionListener(listener);
        valueRadioButton.addActionListener(listener);
        noConstraintRadioButton.addActionListener(listener);
    }

    private void updateButtons() {
        if (groupsTable.getSelectedRowCount() > 0) {
            //SPLIT button
            int groupNumber = -1;
            boolean enableSplit = true;

            // all selected rows are same group?
            for (int i : groupsTable.getSelectedRows()) {
                if (groupNumber < 0) {
                    groupNumber = (int) tableModel.getValueAt(i, 2);
                } else if (groupNumber != (int) tableModel.getValueAt(i, 2)) {
                    enableSplit = false;
                    break;
                }
            }

            // unselected of same group?
            if (groupsTable.getSelectedRowCount() == 1) {
                enableSplit = false;
                for (int i = 0; i < groupsTable.getRowCount(); i++) {
                    if (groupsTable.isRowSelected(i)) {
                        continue;
                    }

                    if (groupNumber == (int) tableModel.getValueAt(i, 2)) {
                        enableSplit = true;
                        break;
                    }
                }
            }

            splitButton.setEnabled(enableSplit);

            //MERGE button
            mergeButton.setEnabled(!enableSplit && groupsTable.getSelectedRowCount() > 1);
        } /*else {
            mergeButton.setEnabled(false);
            splitButton.setEnabled(false);
        }*/

        //ADD button
        boolean enableAdd = true;
        if (getSelectedGroupingType().equals(DimensionGroupEnum.Annotation)) {
            enableAdd = !removedItems.isEmpty();
        }
        addButton.setEnabled(enableAdd);

        //Remove button
        removeButton.setEnabled(groupsTable.getSelectedRowCount() > 0);

    }

    private void performSplit() {

        int currentGroupNumber = (int) tableModel.getValueAt(groupsTable.getSelectedRows()[0], 2);
        int occurrences = 0;

        for (int i = 0; i < groupsTable.getRowCount(); i++) {
            if (currentGroupNumber == (int) tableModel.getValueAt(i, 2)) {
                occurrences++;
            }
        }

        boolean totalSplit = occurrences == groupsTable.getSelectedRowCount();
        int newGroups = 1;

        for (int i = 0; i < groupsTable.getRowCount(); i++) {
            int g = (int) tableModel.getValueAt(i, 2);
            if (g > currentGroupNumber | groupsTable.isRowSelected(i)) {
                groupsTable.setValueAt(g + newGroups, i, 2);
                newGroups = totalSplit && groupsTable.isRowSelected(i) ? ++newGroups : newGroups;
            }
        }
        tableModel.fireTableDataChanged();
    }

    private void performMerge() {

        int groupNumber = -1;
        for (int i : groupsTable.getSelectedRows()) {
            if (groupNumber < 0) {
                groupNumber = (int) groupsTable.getValueAt(i, 2);
            } else {
                groupsTable.setValueAt(groupNumber, i, 2);
            }
        }
        tableModel.fireTableDataChanged();
    }

    private void initGroups() {

        DimensionGroup[] newGroups = new DimensionGroup[0];

        if (getSelectedGroupingType().equals(DimensionGroupEnum.Annotation)) {
            newGroups = initAnnotationGroups();
            if (newGroups == null) {
                setSelectedGroupingType(groupingType);
                return;
            }
        }

        removedItems.clear();
        tableModel.setGroups(newGroups);
        updateGroupingType();
    }

    private DimensionGroup[] initAnnotationGroups() {
        DimensionGroup[] newGroups;
        HeatmapDimension hdim = dimensionCb.getSelectedItem().equals("Columns") ?
                heatmap.getColumns() : heatmap.getRows();

        if (hdim.getAnnotations() == null) {
            setMessage(MessageStatus.WARN, "No annotations found");
            return null;
        }

        PatternSourcePage page = new PatternSourcePage(hdim, true);
        PageDialog dlg = new PageDialog(Application.get(), page);
        dlg.setVisible(true);
        if (dlg.isCancelled()) {
            return null;
        }

        // get all clusters from $pattern

        ClusteringData data = new AnnPatClusteringData(hdim, page.getPattern());
        ClusteringResults results = new AnnPatClusteringMethod().cluster(data, new DefaultProgressMonitor());

        List<DimensionGroup> annGroups = new ArrayList<>();
        for (String groupAnnotationPattern : results.getClusters()) {
            DimensionGroupAnnotation g = new DimensionGroupAnnotation(
                    groupAnnotationPattern,
                    new GroupByLabelPredicate(
                            hdim,
                            groupAnnotationPattern,
                            new PatternFunction(page.getPattern(), hdim.getAnnotations()))
            );
            annGroups.add(g);
        }
        newGroups = annGroups.toArray(new DimensionGroup[annGroups.size()]);
        return newGroups;
    }

    private void createValueGroup() {
        String[] ops = new String[]{Operator.AND.getAbbreviation(), Operator.OR.getAbbreviation()};
        DataIntegrationCriteriaDialog dlg =
                new DataIntegrationCriteriaDialog(
                        Application.get(),
                        heatmap.getLayers(),
                        CutoffCmp.comparators,
                        ops,
                        null,
                        "Group " + Integer.toString(tableModel.getRowCount() + 1));
        dlg.setVisible(true);
        if (dlg.isCancelled()) {
            return;
        }
        List<DataIntegrationCriteria> criteria = dlg.getCriteriaList();
        tableModel.addGroup(
                new DimensionGroupValue(dlg.getGroupName(), new GroupByValuePredicate(criteria, null))
        );
    }

    private void removeSelected() {
        if (getSelectedGroupingType().equals(DimensionGroupEnum.Annotation)) {
            for (int cg : groupsTable.getSelectedRows()) {
                removedItems.add(tableModel.getGroupAt(cg));
            }

        }
        tableModel.removeGroup(groupsTable.getSelectedRows());
    }


    public void addGroups(DimensionGroupValue... groups) {
        tableModel.setGroups(groups);
    }


    private static class TestElement {
        public final Test test;

        public TestElement(Test test) {
            this.test = test;
        }

        @Override
        public String toString() {
            return this.test.getName();
        }

        public Test getTest() {
            return this.test;
        }
    }

    private static class MTCElement {
        public final MTC mtc;

        public MTCElement(MTC mtc) {
            this.mtc = mtc;
        }

        @Override
        public String toString() {
            return this.mtc.getName();
        }

        public MTC getMTC() {
            return this.mtc;
        }
    }

    public class AttrOption {
        private String name;
        private IMatrixLayer attr;

        /**
         * @noinspection UnusedDeclaration
         */
        public AttrOption(String name) {
            this.name = name;
        }

        public AttrOption(IMatrixLayer attr) {
            this.attr = attr;
        }

        public IMatrixLayer getAttr() {
            return attr;
        }

        @Override
        public String toString() {
            return attr != null ? attr.getName() : name;
        }
    }


    public void setSelectedGroupingType(DimensionGroupEnum groupingType) {
        if (groupingType.equals(DimensionGroupEnum.Annotation)) {
            annotationRadioButton.setSelected(true);
        } else if (groupingType.equals(DimensionGroupEnum.Value)) {
            valueRadioButton.setSelected(true);
        } else if (groupingType.equals(DimensionGroupEnum.Free)) {
            noConstraintRadioButton.setSelected(true);
        }
        this.groupingType = groupingType;
    }

    public DimensionGroupEnum getSelectedGroupingType() {
        if (annotationRadioButton.isSelected()) {
            return DimensionGroupEnum.Annotation;
        } else if (valueRadioButton.isSelected()) {
            return DimensionGroupEnum.Value;
        } else if (noConstraintRadioButton.isSelected()) {
            return DimensionGroupEnum.Free;
        }
        return null;
    }

    private void updateGroupingType() {
        if (annotationRadioButton.isSelected()) {
            this.groupingType = DimensionGroupEnum.Annotation;
        } else if (valueRadioButton.isSelected()) {
            this.groupingType = DimensionGroupEnum.Value;
        } else if (noConstraintRadioButton.isSelected()) {
            this.groupingType = DimensionGroupEnum.Free;
        }
    }

    @Override
    public JComponent createControls() {
        return panel1;
    }


    public void setAttributes(IMatrixLayers attrs) {

        if (attrs != null) {
            AttrOption[] attrOptions = new AttrOption[attrs.size()];

            for (int i = 0; i < attrs.size(); i++)
                attrOptions[i] = new AttrOption(attrs.get(i));

            layerCb.setModel(new DefaultComboBoxModel(attrOptions));
            layerCb.setSelectedIndex(0);
            layerCb.setEnabled(true);
            layerCb.setVisible(true);
            //attributeLabel.setVisible(true);
        } else {
            dissableAttrCb();
        }
    }

    private void dissableAttrCb() {
        layerCb.setModel(new DefaultComboBoxModel());
        layerCb.setEnabled(false);
        layerCb.setVisible(false);
        //attributeLabel.setVisible(false);
    }

    public int getAttributeIndex() {
        return layerCb.getSelectedIndex();
    }


}
