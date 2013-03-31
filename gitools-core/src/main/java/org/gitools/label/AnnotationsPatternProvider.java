package org.gitools.label;

import org.gitools.utils.textpatt.TextPattern;
import org.gitools.matrix.model.AnnotationMatrix;

public class AnnotationsPatternProvider implements LabelProvider {

	private LabelProvider labelProvider;
	private TextPattern pat;
	private AnnotationsResolver resolver;

	public AnnotationsPatternProvider(LabelProvider labelProvider, AnnotationMatrix am, String pattern) {
		this.labelProvider = labelProvider;
		this.pat = new TextPattern(pattern);
		this.resolver = new AnnotationsResolver(labelProvider, am);
	}

	@Override
	public int getCount() {
		return labelProvider.getCount();
	}

	@Override
	public String getLabel(int index) {
		resolver.setIndex(index);
		return pat.generate(resolver);
	}
}
