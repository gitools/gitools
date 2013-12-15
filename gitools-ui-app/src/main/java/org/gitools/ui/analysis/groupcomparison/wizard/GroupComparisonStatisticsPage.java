package org.gitools.ui.analysis.groupcomparison.wizard;

import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupEnum;
import org.gitools.analysis.stats.mtc.BenjaminiHochbergFdr;
import org.gitools.analysis.stats.mtc.Bonferroni;
import org.gitools.analysis.stats.mtc.MTC;
import org.gitools.analysis.stats.test.MannWhitneyWilxoxonTest;
import org.gitools.analysis.stats.test.Test;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;


public class GroupComparisonStatisticsPage extends AbstractWizardPage {


    private JPanel panel1;
    private JComboBox layerCb;
    private JComboBox groupingTypeCb;
    private JLabel DataLabel;
    private JComboBox dimensionCb;
    private JComboBox testCb;
    private JComboBox mtcCb;
    private JTextPane testExplanation;

    public GroupComparisonStatisticsPage() {
        super();

        setTitle("Select data and statistical test");

        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));

        setComplete(true);

        testCb.setModel(new DefaultComboBoxModel(new TestElement[]{new TestElement(new MannWhitneyWilxoxonTest())}));

        mtcCb.setModel(new DefaultComboBoxModel(new MTCElement[]{new MTCElement(new BenjaminiHochbergFdr()), new MTCElement(new Bonferroni())}));
        mtcCb.setSelectedIndex(1);

        groupingTypeCb.setModel(new DefaultComboBoxModel(DimensionGroupEnum.values()));

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

    public Test getTest() {
        TestElement testElement = (TestElement) testCb.getModel().getSelectedItem();
        return testElement.getTest();
    }

    public MTC getMtc() {
        MTCElement mtcElement = (MTCElement) mtcCb.getModel().getSelectedItem();
        return mtcElement.getMTC();
    }


    public DimensionGroupEnum getColumnGrouping() {
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
