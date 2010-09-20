/*
 *  Copyright 2010 chris.
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

package org.gitools.ui.analysis.combination.editor;

import java.util.List;
import org.apache.velocity.VelocityContext;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.persistence.PersistenceManager;

public class CombinationAnalysisEditor extends AnalysisDetailsEditor<CombinationAnalysis> {

	public CombinationAnalysisEditor(CombinationAnalysis analysis) {
		super(analysis, "/vm/analysis/combination.vm", null);
	}

	@Override
	protected void prepareContext(VelocityContext context) {
		String combOf = "columns";
		if (analysis.isTransposeData())
			combOf = "rows";
		context.put("combinationOf", combOf);

		PersistenceManager.FileRef fileRef = PersistenceManager.getDefault()
				.getEntityFileRef(analysis.getData());

		context.put("dataFile",
				fileRef != null ? fileRef.getFile().getAbsolutePath() : null);

		fileRef = PersistenceManager.getDefault()
				.getEntityFileRef(analysis.getGroupsMap());

		String groupsFile = fileRef != null ? fileRef.getFile().getAbsolutePath()
				: "Not specified. All " + combOf + " are combined";
		context.put("groupsFile", groupsFile);

		String sizeAttr = analysis.getSizeAttrName();
		if (sizeAttr == null || sizeAttr.isEmpty())
			sizeAttr = "Constant value of 1";
		context.put("sizeAttr", sizeAttr);

		String pvalueAttr = analysis.getPvalueAttrName();
		if (pvalueAttr == null || pvalueAttr.isEmpty()) {
			List<IElementAttribute> attrs = analysis.getData().getCellAttributes();
			if (attrs.size() > 0)
				pvalueAttr = attrs.get(0).getName();
		}
		context.put("pvalueAttr", pvalueAttr);
	}
}
