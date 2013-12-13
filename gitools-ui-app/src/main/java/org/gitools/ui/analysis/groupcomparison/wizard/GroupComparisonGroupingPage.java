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
import org.gitools.analysis.stats.mtc.MTC;
import org.gitools.analysis.stats.test.Test;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.matrix.filter.PatternFunction;
import org.gitools.ui.IconNames;
import org.gitools.ui.heatmap.panel.settings.headers.SpinnerCellEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizardPage;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.wizard.common.PatternSourcePage;
import org.gitools.utils.progressmonitor.DefaultProgressMonitor;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class GroupComparisonGroupingPage extends AbstractWizardPage {


    private JPanel panel1;
    private JComboBox layerCb;
    private JComboBox groupingTypeCb;
    private JComboBox dimensionCb;
    private JButton addButton;
    private JButton removeButton;
    private JTable groupsTable;
    private JLabel dataLabel;


    private DimensionGroupTableModel tableModel = new DimensionGroupTableModel();
    private List<DimensionGroup> removedItems = new ArrayList<DimensionGroup>();
    private Heatmap heatmap;


    public GroupComparisonGroupingPage(Heatmap heatmap) {
        super();

        this.heatmap = heatmap;

        setTitle("Select data and statistical test");

        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));


        groupingTypeCb.setModel(new DefaultComboBoxModel(DimensionGroupEnum.values()));
        layerCb.setModel(new DefaultComboBoxModel(heatmap.getLayers().getIds()));


        groupsTable.setModel(tableModel);

        setTitle("Groups");
        setMessage("Add / Remove groups and merge by selecting the same group number.");

        TableColumnModel columnModel = groupsTable.getColumnModel();

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
        groupsTable.setRowHeight(30);

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelected();
            }
        });


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getGroupingType().equals(DimensionGroupEnum.Annotation)) {
                    //TODO: create Dialog with removedItems
                } else if (getGroupingType().equals(DimensionGroupEnum.Free)) {
                    //TODO: create Dialog with paste labels
                } else if (getGroupingType().equals(DimensionGroupEnum.Value)) {
                    //TODO: create Dialog with binary filters
                }
            }
        });


        dimensionCb.setModel(new DefaultComboBoxModel(new String[]{"Columns", "Rows"}));


        groupingTypeCb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initGroups();
            }
        });

        setComplete(true);

    }

    private void initGroups() {

        DimensionGroup[] newGroups = new DimensionGroup[0];

        if (getGroupingType().equals(DimensionGroupEnum.Annotation)) {
            HeatmapDimension hdim = dimensionCb.getSelectedItem().equals("Columns") ?
                    heatmap.getColumns() : heatmap.getRows();

            PatternSourcePage page = new PatternSourcePage(hdim, true);
            PageDialog dlg = new PageDialog(Application.get(), page);
            dlg.setVisible(true);
            if (dlg.isCancelled()) {
                return;
            }


            // get all clusters from $pattern

            ClusteringData data = new AnnPatClusteringData(hdim, page.getPattern());
            ClusteringResults results = new AnnPatClusteringMethod().cluster(data, new DefaultProgressMonitor());

            List<DimensionGroup> annGroups = new ArrayList<>();
            for (String groupAnnotationPattern : results.getClusters()) {
                System.out.println(groupAnnotationPattern);
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
        }


        removedItems.clear();
        tableModel.setGroups(newGroups);

    }

    private void removeSelected() {
        if (getGroupingType().equals(DimensionGroupEnum.Annotation)) {
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


    public DimensionGroupEnum getGroupingType() {
        return (DimensionGroupEnum) groupingTypeCb.getSelectedItem();
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
