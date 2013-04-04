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
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.component.ColorChooserLabel;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class BinaryElementDecoratorPanel extends AbstractElementDecoratorPanel
{

    private static final long serialVersionUID = -930914489603614155L;

    private BinaryElementDecorator decorator;

    private JComboBox valueCb;

    private JComboBox cmpCb;

    private JTextField cutoffTf;

    private ColorChooserLabel colorCc;

    private ColorChooserLabel nonSigColorCc;

    private ColorChooserLabel emptyCc;

    public BinaryElementDecoratorPanel(Heatmap model)
    {
        super(model);

        this.decorator = (BinaryElementDecorator) getDecorator();

        final IElementAdapter adapter = decorator.getAdapter();

        valueProperties = new ArrayList<IndexedProperty>();
        loadAllProperties(valueProperties, adapter);

        createComponents();
    }

    private void createComponents()
    {
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

        cmpCb = new JComboBox(CutoffCmp.comparators);

        cmpCb.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(@NotNull ItemEvent e)
            {
                if (e.getStateChange() == ItemEvent.SELECTED)
                {
                    cmpChanged();
                }
            }
        });

        cutoffTf = new JTextField(Double.toString(decorator.getCutoff()));
        cutoffTf.getDocument().addDocumentListener(new DocumentListener()
        {
            @Override
            public void changedUpdate(DocumentEvent e)
            {
                cutoffChanged();
            }

            @Override
            public void insertUpdate(DocumentEvent e)
            {
                cutoffChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                cutoffChanged();
            }
        });

        colorCc = new ColorChooserLabel(decorator.getColor());
        colorCc.setToolTipText("Condition color");
        colorCc.addColorChangeListener(new ColorChangeListener()
        {
            @Override
            public void colorChanged(Color color)
            {
                decorator.setColor(color);
            }
        });

        nonSigColorCc = new ColorChooserLabel(decorator.getNonSignificantColor());
        nonSigColorCc.setToolTipText("Non condition color");
        nonSigColorCc.addColorChangeListener(new ColorChangeListener()
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

        Box box01 = new Box(BoxLayout.X_AXIS);
        box01.add(cmpCb);
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(cutoffTf);
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(colorCc);
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(nonSigColorCc);
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

        CutoffCmp cmp = decorator.getCutoffCmp();
        for (int i = 0; i < cmpCb.getItemCount(); i++)
        {
            CutoffCmp cc = (CutoffCmp) cmpCb.getItemAt(i);
            if (cc.equals(cmp))
            {
                cmpCb.setSelectedIndex(i);
            }
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
    }

    private void changeDecorator()
    {

        ElementDecorator elementDecorator = model.getActiveCellDecorator();
        if (elementDecorator instanceof BinaryElementDecorator)
        {
            this.decorator = (BinaryElementDecorator) elementDecorator;
        }
        else
        {
            return;
        }


        colorCc.setColor(decorator.getColor());
        nonSigColorCc.setColor(decorator.getNonSignificantColor());
        emptyCc.setColor(decorator.getEmptyColor());

        cutoffTf.setText(Double.toString(decorator.getCutoff()));
        cmpCb.setSelectedItem(decorator.getCutoffCmp());


    }

    protected void cmpChanged()
    {
        CutoffCmp cc = (CutoffCmp) cmpCb.getSelectedItem();
        decorator.setCutoffCmp(cc);
    }

    protected void cutoffChanged()
    {
        try
        {
            double cutoff = Double.parseDouble(cutoffTf.getText());
            decorator.setCutoff(cutoff);

            AppFrame.get().setStatusText("Cutoff changed to " + cutoff);
        } catch (Exception e)
        {
            AppFrame.get().setStatusText("Invalid cutoff.");
        }
    }
}
