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

import org.gitools.analysis.groupcomparison.format.math33Preview.CombinatoricsUtils;
import org.gitools.analysis.stats.test.MannWhitneyWilcoxonTest;
import org.gitools.analysis.stats.test.Test;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

import javax.swing.*;


public class GroupComparisonStatisticsPage extends AbstractWizardPage {


    private JPanel panel1;
    private JComboBox testCb;
    private JComboBox mtcCb;
    private JTextPane testExplanation;
    private int groupNumber;

    public GroupComparisonStatisticsPage() {
        super();

        setTitle("Select data and statistical test");

        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_METHOD, 96));

        setComplete(true);

        testCb.setModel(new DefaultComboBoxModel(new TestElement[]{new TestElement(new MannWhitneyWilcoxonTest())}));

        mtcCb.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Bonferroni", "Benjamini Hochberg FDR"}));
        mtcCb.setSelectedIndex(1);
        testExplanation.setContentType("text/html");
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
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

    public class AttrOption {
        private String name;
        private IMatrixLayer attr;

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

    @Override
    public void updateControls() {

        if (testCb.getSelectedIndex() == 0) {

            testExplanation.setText("<html><body style=\"font-size: 9px;\">" +
                    "The two-sample Mann-Whitney-Wilcoxon rank sum test will be carried out" +
                    " for each pair of groups. The <b>null hypothesis</b> is that the distributions of Group 1 and Group 2 do " +
                    "not differ. The <b>alternative</b> is that they do: The one-sided alternative \"greater\" is that " +
                    "Group 1 is shifted to the right of Group 2 resulting in a low right-tail p-value. " +
                    "The one-sided alternative \"less\" is that " +
                    "Group 1 is shifted to the left of Group 2 resulting in a low left-tail p-value." +
                    "<br><img src=\"" + IconNames.ANALYSIS_IMAGE_MANN_WHITNEY_WILCOXON.toString()
                    + "\" > <br>" +
                    "Above, Expression value distribution of Group 1 (Gain) is shifted to the right of Group 2 (Loss)</body></html>");

            double v = CombinatoricsUtils.binomialCoefficientDouble(groupNumber, 2);
            if (v < 101) {
                setMessage(MessageStatus.INFO, "You will be calculating " + String.valueOf(Math.round(v)) + " group combinations");
            } else {
                setMessage(MessageStatus.WARN, "This would yield " + String.valueOf(v) + " group combinations. Gitools" +
                        " refuses to do that much work.");
            }
        }
    }

    public Test getTest() {
        TestElement testElement = (TestElement) testCb.getModel().getSelectedItem();
        return testElement.getTest();
    }

    public String getMtc() {
        switch (mtcCb.getSelectedIndex()) {
            case 0:
                return "bonferroni";
            case 1:
                return "bh";
        }
        return "bh";
    }


    @Override
    public JComponent createControls() {
        return panel1;
    }


}
