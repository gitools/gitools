package org.gitools.kegg.modules;

import org.gitools.modules.importer.Category;
import org.gitools.modules.importer.ModuleCategory;

public class EnsemblKeggModuleCategory extends Category implements ModuleCategory {

	public EnsemblKeggModuleCategory(String db, String id, String name) {
		super(db, id, name);
	}
}
