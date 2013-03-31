/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.panels.decorator;

import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.matrix.model.element.IElementAdapter;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.CategoricalElementDecorator;
import org.gitools.ui.dialog.EditCategoricalScaleDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.component.ColorChooserLabel;
import org.gitools.ui.platform.component.ColorChooserLabel.ColorChangeListener;
import org.gitools.ui.platform.dialog.AbstractDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class CategoricalElementDecoratorPanel extends AbstractElementDecoratorPanel {

	private static final long serialVersionUID = 8422331422677024364L;

	private CategoricalElementDecorator decorator;

	private JComboBox valueCb;

	private ColorChooserLabel emptyCc;
    
    private String categoriesLabelText = " Categories:";
    private JLabel categoriesLabel = new JLabel(categoriesLabelText);

	public CategoricalElementDecoratorPanel(Heatmap model) {
		super(model);
	
		this.decorator = (CategoricalElementDecorator) model.getActiveCellDecorator();

		final IElementAdapter adapter = decorator.getAdapter();
		
		valueProperties = new ArrayList<IndexedProperty>();
		loadAllProperties(valueProperties, adapter);
		
		createComponents();
	}

	private void createComponents() {
		valueCb = new JComboBox(new DefaultComboBoxModel(valueProperties.toArray()));
		
		valueCb.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					valueChanged();
				}
			}
		});
	

		emptyCc = new ColorChooserLabel(decorator.getEmptyColor());
		emptyCc.setToolTipText("Empty cell color");
		emptyCc.addColorChangeListener(new ColorChangeListener() {
			@Override public void colorChanged(Color color) {
				decorator.setEmptyColor(color); }
		});

		refresh();

		Dimension boxSpace = new Dimension(3, 3);

		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(new JLabel("Value"));
		add(valueCb);
        
        int catCount = decorator.getCategoriesCount();
        categoriesLabel.setText(catCount + categoriesLabelText);
        Box box02 = new Box(BoxLayout.LINE_AXIS);
        box02.add(categoriesLabel);
        JButton categoryEditBtn = new JButton("edit");
        categoryEditBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editCategoricalColorScale();
            }
        });
        box02.add(categoryEditBtn);
        add(box02);



        Box box01 = new Box(BoxLayout.LINE_AXIS);
        box01.add(new JLabel(" Empty"));
        box01.add(Box.createRigidArea(boxSpace));
        box01.add(emptyCc);
        add(box01);
	}

    private void editCategoricalColorScale() {


        CategoricalElementDecorator elementDecorator = (CategoricalElementDecorator) model.getActiveCellDecorator();
        CategoricalColorScale scale =
                (CategoricalColorScale) elementDecorator.getScale();
        ColorScalePoint[] scalePoints = scale.getPointObjects();

        ColoredLabel[] coloredLabels = new ColoredLabel[scalePoints.length];
        int index = 0;
        for (ColorScalePoint sp : scalePoints) {
            coloredLabels[index] = new ColoredLabel(sp.getValue(),sp.getName(),sp.getColor());
            index++;
        }

        EditCategoricalScaleDialog dialog = new EditCategoricalScaleDialog(AppFrame.instance(), coloredLabels);
        dialog.getPage().setValueMustBeNumeric(true);
        dialog.setVisible(true);
        if (dialog.getReturnStatus() == AbstractDialog.RET_OK) {
            coloredLabels = dialog.getPage().getColoredLabels();

            ColorScalePoint[] newScalePoints = new ColorScalePoint[coloredLabels.length];
            index = 0;
            for (ColoredLabel cl : coloredLabels) {
                newScalePoints[index] = new ColorScalePoint( Double.parseDouble(cl.getValue()),
                                                             cl.getColor(),
                                                             cl.getDisplayedLabel());
                index++;
            }

            updateCategoriesLabel();
            elementDecorator.setCategories(newScalePoints);
        }
    }

    private void updateCategoriesLabel() {
        int catCount = decorator.getCategoriesCount();
        categoriesLabel.setText(catCount + categoriesLabelText);
    }

    private void refresh() {
		for (int i = 0; i < valueProperties.size(); i++)
			if (valueProperties.get(i).getIndex() == decorator.getValueIndex())
				valueCb.setSelectedIndex(i);
		
		getTable().setSelectedPropertyIndex(decorator.getValueIndex());
	}
	
	private void valueChanged() {
		IndexedProperty propAdapter = 
			(IndexedProperty) valueCb.getSelectedItem();

		model.switchActiveCellDecorator(propAdapter.getIndex());
		changeDecorator();
		
		decorator.setValueIndex(propAdapter.getIndex());
        updateCategoriesLabel();
		getTable().setSelectedPropertyIndex(propAdapter.getIndex());
	}

	private void changeDecorator() {

        ElementDecorator elementDecorator = model.getActiveCellDecorator();
        if (elementDecorator instanceof  CategoricalElementDecorator)
    		this.decorator = (CategoricalElementDecorator) elementDecorator;
        else
            return;

		emptyCc.setColor(decorator.getEmptyColor());

	}


	/*protected void maxValueChanged() {
		try {
			double value = Double.parseDouble(maxValTxt.getText());
			//decorator.setMaxValue(value);

			AppFrame.instance().setStatusText("Maximum value changed to " + value);
		}
		catch (Exception e) {
			AppFrame.instance().setStatusText("Invalid value.");
		}
	}                  */
}
