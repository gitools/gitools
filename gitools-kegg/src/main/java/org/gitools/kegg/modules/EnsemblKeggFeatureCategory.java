package org.gitools.kegg.modules;

import org.gitools.modules.importer.Category;
import org.gitools.modules.importer.FeatureCategory;

public class EnsemblKeggFeatureCategory extends Category implements FeatureCategory {

	public EnsemblKeggFeatureCategory(String section, String id, String name) {
		super(section, id, name);
	}
}
