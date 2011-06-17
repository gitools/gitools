package org.gitools.label;

import edu.upf.bg.textpatt.TextPattern;
import org.gitools.matrix.model.AnnotationMatrix;

public class AnnotationsResolver implements TextPattern.VariableValueResolver {

	private LabelProvider labelProvider;
	private AnnotationMatrix am;
	private int index;

	public AnnotationsResolver(LabelProvider labelProvider, AnnotationMatrix am) {
		this.labelProvider = labelProvider;
		this.am = am;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String resolveValue(String variableName) {
		String label = labelProvider.getLabel(index);
		if (variableName.equalsIgnoreCase("id"))
			return label;

		int annRow = am != null ? am.getRowIndex(label) : -1;
		if (annRow == -1)
			return "${" + variableName + "}";

		int annCol = am.getColumnIndex(variableName);
		return am.getCell(annRow, annCol);
	}
}
