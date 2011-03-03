/*
 *  Copyright 2011 chris.
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

package org.gitools.matrix.model;

import edu.upf.bg.textpatt.TextPattern.VariableValueResolver;

public class AnnotationResolver implements VariableValueResolver {

		private AnnotationMatrix am;

		private String label;
		private int annRow;

		public AnnotationResolver(AnnotationMatrix am) {
			this(am, null);
		}

		public AnnotationResolver(AnnotationMatrix am, String label) {
			this.am = am;
			if (label != null)
				setLabel(label);
		}

		public final void setLabel(String label) {
			this.label = label;

			if (am != null)
				annRow = am.getRowIndex(label);
		}

		@Override
		public String resolveValue(String variableName) {
			if (variableName.equalsIgnoreCase("id"))
				return label;

			int annCol = am != null ? am.getColumnIndex(variableName) : -1;
			if (annCol == -1)
				return "${" + variableName + "}";

			return am.getCell(annRow, annCol);
		}
}
