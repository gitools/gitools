/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.panels.decorator;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.ZScoreElementDecorator;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.component.ColorChooserLabel;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class ZScoreElementDecoratorPanel extends AbstractElementDecoratorPanel
{

    private static final long serialVersionUID = -7443053984962647946L;

    private List<IndexedProperty> valueProperties;
    private List<IndexedProperty> corrValueProperties;

    private ZScoreElementDecorator decorator;

    private JComboBox valueCb;
    private JCheckBox showCorrChkBox;
    private JComboBox corrValueCb;
    private JTextField sigLevelTb;

    private ColorChooserLabel lminColorCc;
    private ColorChooserLabel lmaxColorCc;
    private ColorChooserLabel rminColorCc;
    private ColorChooserLabel rmaxColorCc;
    private ColorChooserLabel nsigColorCc;
    private ColorChooserLabel emptyCc;

    public ZScoreElementDecoratorPanel(@NotNull Heatmap model)
    {
        super(model);

        this.decorator = (ZScoreElementDecorator) model.getActiveCellDecorator();

        final IElementAdapter adapter = decorator.getAdapter();

        int numProps = adapter.getPropertyCount();

        valueProperties = new ArrayList<IndexedProperty>();
        corrValueProperties = new ArrayList<IndexedProperty>();
        for (int i = 0; i < numProps; i++)
        {
            final IElementAttribute property = adapter.getProperty(i);

            if (property.getId().endsWith("z-score"))
            {
                valueProperties.add(new IndexedProperty(i, property));
            }
            if (property.getId().startsWith("corrected"))
            {
                corrValueProperties.add(new IndexedProperty(i, property));
            }
        }

        if (valueProperties.size() == 0)
        {
            loadAllProperties(valueProperties, adapter);
            loadAllProperties(corrValueProperties, adapter);
        }
        else if (corrValueProperties.size() == 0)
        {
            loadAllProperties(corrValueProperties, adapter);
        }

        createComponents();
    }

    private void createComponents()
    {

        // value combo box

        valueCb = new JComboBox(new DefaultComboBoxModel(valueProperties.toArray()));

        valueCb.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(@NotNull ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    valueChanged();
                }
            }
        });

        // show correction check box

        showCorrChkBox = new JCheckBox();
        showCorrChkBox.setText("Filter sig. by");
        showCorrChkBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                showCorrectionChecked();
            }
        });

        // corrected value combo box

        corrValueCb = new JComboBox(new DefaultComboBoxModel(corrValueProperties.toArray()));

        corrValueCb.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(@NotNull ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    corrValueChanged();
                }
            }
        });

        // significance level

        sigLevelTb = new JTextField(Double.toString(decorator.getSignificanceLevel()));
        sigLevelTb.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                sigLevelChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                sigLevelChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                sigLevelChanged();
            }
        });

		/*sigLevelColorCc = new ColorChooserLabel(decorator.getColor());
        sigLevelColorCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setNonSigColor(color); }
		});*/

        lminColorCc = new ColorChooserLabel(decorator.getLeftMinColor());
        lminColorCc.setToolTipText("Left side lowest value color");
        lminColorCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setLeftMinColor(color);
            }
        });

        lmaxColorCc = new ColorChooserLabel(decorator.getLeftMaxColor());
        lmaxColorCc.setToolTipText("Left side greatest value color");
        lmaxColorCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setLeftMaxColor(color);
            }
        });

        nsigColorCc = new ColorChooserLabel(decorator.getNonSignificantColor());
        nsigColorCc.setToolTipText("Non significant value color");
        nsigColorCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setNonSignificantColor(color);
            }
        });

        rminColorCc = new ColorChooserLabel(decorator.getRightMinColor());
        rminColorCc.setToolTipText("Right side lowest value color");
        rminColorCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setRightMinColor(color);
            }
        });

        rmaxColorCc = new ColorChooserLabel(decorator.getRightMaxColor());
        rmaxColorCc.setToolTipText("Right side greatest value color");
        rmaxColorCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setRightMaxColor(color);
            }
        });

        emptyCc = new ColorChooserLabel(decorator.getEmptyColor());
        emptyCc.setToolTipText("Empty cell color");
        emptyCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setEmptyColor(color);
            }
        });

        refresh();

        Dimension boxSpace = new Dimension(3, 3);

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(new JLabel("Value"));
        add(valueCb);
        add(showCorrChkBox);
        add(corrValueCb);
        add(new JLabel("Sig. level"));
        add(sigLevelTb);

        Box box01 = new Box(BoxLayout.X_AXIS);
        box01.add(lminColorCc);
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(lmaxColorCc);
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(nsigColorCc);
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(rminColorCc);
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(rmaxColorCc);
        add(box01);

        Box box02 = new Box(BoxLayout.X_AXIS);
        box02.add(new JLabel("Empty"));
        box02.add(Box.createRigidArea(boxSpace));
        box02.add(emptyCc);
        add(box02);
    }

    private void refresh()
    {
        for (int i = 0; i < valueProperties.size(); i++)
            if (valueProperties.get(i).getIndex() == decorator.getValueIndex())
            {
                valueCb.setSelectedIndex(i);
            }

        getTable().setSelectedPropertyIndex(decorator.getValueIndex());

        showCorrChkBox.setSelected(decorator.getUseCorrection());
        corrValueCb.setEnabled(decorator.getUseCorrection());

        for (int i = 0; i < corrValueProperties.size(); i++)
            if (corrValueProperties.get(i).getIndex() == decorator.getCorrectedValueIndex())
            {
                corrValueCb.setSelectedIndex(i);
            }
    }

    private void valueChanged()
    {

        IndexedProperty propAdapter =
                (IndexedProperty) valueCb.getSelectedItem();

        model.switchActiveCellDecorator(propAdapter.getIndex());
        changeDecorator();

        decorator.setValueIndex(propAdapter.getIndex());
        getTable().setSelectedPropertyIndex(propAdapter.getIndex());

        // search for corresponding corrected value

        int corrIndex = MatrixUtils.correctedValueIndex(
                decorator.getAdapter(), propAdapter.getProperty());

        if (corrIndex >= 0)
        {
            decorator.setCorrectedValueIndex(corrIndex);
        }


        refresh();
    }

    private void changeDecorator()
    {

        ElementDecorator elementDecorator = model.getActiveCellDecorator();
        if (elementDecorator instanceof ZScoreElementDecorator)
        {
            this.decorator = (ZScoreElementDecorator) elementDecorator;
        }
        else
        {
            return;
        }

        lminColorCc.setColor(decorator.getLeftMinColor());
        lmaxColorCc.setColor(decorator.getLeftMaxColor());
        nsigColorCc.setColor(decorator.getNonSignificantColor());
        rminColorCc.setColor(decorator.getRightMinColor());
        rmaxColorCc.setColor(decorator.getRightMaxColor());

        sigLevelTb.setText(Double.toString(decorator.getSignificanceLevel()));
        corrValueCb.setSelectedIndex(decorator.getCorrectedValueIndex());

    }

    private void showCorrectionChecked()
    {
        decorator.setUseCorrection(
                showCorrChkBox.isSelected());

        corrValueCb.setEnabled(
                showCorrChkBox.isSelected());
    }

    protected void corrValueChanged()
    {
        IndexedProperty propAdapter =
                (IndexedProperty) corrValueCb.getSelectedItem();

        decorator.setCorrectedValueIndex(propAdapter.getIndex());
    }

    protected void sigLevelChanged()
    {
        try
        {
            double value = Double.parseDouble(sigLevelTb.getText());
            decorator.setSignificanceLevel(value);

            AppFrame.get().setStatusText("Significance level changed to " + value);
        } catch (Exception e)
        {
            AppFrame.get().setStatusText("Invalid value.");
        }
    }
}
