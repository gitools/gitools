/*
 * #%L
 * org.gitools.ui.app
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.app.analysis.groupcomparison.wizard;

import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupEnum;
import org.gitools.analysis.stats.mtc.BenjaminiHochbergFdr;
import org.gitools.analysis.stats.mtc.Bonferroni;
import org.gitools.analysis.stats.mtc.MTC;
import org.gitools.analysis.stats.test.MannWhitneyWilxoxonTest;
import org.gitools.analysis.stats.test.Test;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.ui.app.IconNames;
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
