/*
 *  Copyright 2010 xavier.
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
package org.gitools.ui.biomart.filter;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.gitools.biomart.restful.model.Filter;
import org.gitools.biomart.restful.model.FilterDescription;
import org.gitools.biomart.restful.model.Option;


public class FilterTextComponent extends FilterComponent {

	
	private JTextField txtField;
	private JTextArea txtArea;
	private File selectedFile;
	private JFileChooser fileChooser;

	private String component;

	FilterTextComponent(FilterDescription d,FilterDescriptionPanel parent) {

		super(d,parent);
		buildComponent();
	}

	FilterTextComponent(Option o, IFilterComponent parentComponent) {
                super(o, parentComponent);
		buildComponent();
	}

//FIXME : Test the way how to initialise components (default value or empty string). Not clear from Biomart
	private void buildComponent() {

		if (filterDescription.getMultipleValues() == null || !filterDescription.getMultipleValues().equals("1")) {
			txtField = new JTextField();
			txtField.setVisible(true);
			txtField.setText( 
					filterDescription.getDefaultValue()!=null ? filterDescription.getDefaultValue() : ""
					);
			component = "Field";

		} else {
			component = "TextArea";
			txtArea = new JTextArea();
			txtArea.setVisible(true);
			txtArea.setText("");
			fileChooser = new JFileChooser();
			fileChooser.setVisible(true);
		}

	}

	@Override
	public Filter getFilter() {

		Filter f = new Filter();
		f.setName(filterDescription.getInternalName());

		if (component.equals("Field")) 
			f.setValue(txtField.getText());
		else
			f.setValue(txtArea.getText().replace("\n", ","));
		
		return f;
	}

	@Override
	//FIXME : Check initial component value (default value or empty string). Not clear from Biomart
	public Boolean hasChanged() {

		if (component.equals("Field")) {
			return (txtField.getText().equals(
					(filterDescription.getDefaultValue()!=null) ? filterDescription.getDefaultValue() : ""
					)
					);
		} else {
			return (txtArea.getText().equals(filterDescription.getValue()));
		}

	}



}
