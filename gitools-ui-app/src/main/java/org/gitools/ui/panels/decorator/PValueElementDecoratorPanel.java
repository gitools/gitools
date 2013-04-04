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
import org.gitools.model.decorator.impl.PValueElementDecorator;
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

public class PValueElementDecoratorPanel extends AbstractElementDecoratorPanel
{

    private static final long serialVersionUID = -7443053984962647946L;

    private List<IndexedProperty> valueProperties;
    private List<IndexedProperty> corrValueProperties;

    private PValueElementDecorator decorator;

    private JComboBox valueCb;
    private JCheckBox showCorrChkBox;
    private JComboBox corrValueCb;
    private JTextField sigLevelTb;

    private ColorChooserLabel minColorCc;
    private ColorChooserLabel maxColorCc;
    private ColorChooserLabel nsigColorCc;
    private ColorChooserLabel emptyCc;

    public PValueElementDecoratorPanel(Heatmap model)
    {
        super(model);

        this.decorator = (PValueElementDecorator) getDecorator();

        final IElementAdapter adapter = decorator.getAdapter();

        int numProps = adapter.getPropertyCount();

        valueProperties = new ArrayList<IndexedProperty>();
        corrValueProperties = new ArrayList<IndexedProperty>();
        for (int i = 0; i < numProps; i++)
        {
            final IElementAttribute property = adapter.getProperty(i);

            if (property.getId().endsWith("p-value")
                    /*&& !property.getId().startsWith("corrected")*/)
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

        minColorCc = new ColorChooserLabel(decorator.getMinColor());
        minColorCc.setToolTipText("Lower significant p-value color");
        minColorCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setMinColor(color);
            }
        });

        maxColorCc = new ColorChooserLabel(decorator.getMaxColor());
        maxColorCc.setToolTipText("Greater significant p-value color");
        maxColorCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setMaxColor(color);
            }
        });

        nsigColorCc = new ColorChooserLabel(decorator.getNonSignificantColor());
        nsigColorCc.setToolTipText("Non significant p-value color");
        nsigColorCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setNonSignificantColor(color);
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

        Box box01 = new Box(BoxLayout.X_AXIS);
        box01.add(new JLabel("Sig. level"));
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(sigLevelTb);
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(minColorCc);
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(maxColorCc);
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(nsigColorCc);
        add(box01);

        add(new JLabel("Empty"));
        add(emptyCc);
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

        //decorator.setValueIndex(propAdapter.getIndex());

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

        //showCorrChkBox.setEnabled(corrIndex >= 0);

        refresh();
    }

    private void changeDecorator()
    {

        ElementDecorator elementDecorator = model.getActiveCellDecorator();
        if (elementDecorator instanceof PValueElementDecorator)
        {
            this.decorator = (PValueElementDecorator) elementDecorator;
        }
        else
        {
            return;
        }

        minColorCc.setColor(decorator.getMinColor());
        maxColorCc.setColor(decorator.getMaxColor());
        emptyCc.setColor(decorator.getEmptyColor());
        nsigColorCc.setColor(decorator.getNonSignificantColor());

        sigLevelTb.setText(Double.toString(decorator.getSignificanceLevel()));
        //corrValueCb.setSelectedIndex(decorator.getCorrectedValueIndex());

        minColorCc.setColor(decorator.getMinColor());
        maxColorCc.setColor(decorator.getMaxColor());
        emptyCc.setColor(decorator.getEmptyColor());

        sigLevelTb.setText(Double.toString(decorator.getSignificanceLevel()));

    }

    private void showCorrectionChecked()
    {
        /*IndexedProperty propAdapter =
            (IndexedProperty) valueCb.getSelectedItem();
		
		if (propAdapter != null) {
			int corrIndex = TableUtils.correctedValueIndex(
					decorator.getAdapter(), propAdapter.getProperty());
			
			if (corrIndex >= 0)
				decorator.setCorrectedValueIndex(corrIndex);
		}*/

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
