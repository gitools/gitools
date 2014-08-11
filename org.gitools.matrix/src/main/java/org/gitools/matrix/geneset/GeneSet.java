/*
 * #%L
 * gitools-core
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
package org.gitools.matrix.geneset;


import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.api.resource.SemanticVersion;

import java.util.LinkedHashSet;

public class GeneSet extends LinkedHashSet<String> implements IResource {

    private IResourceLocator locator;
    private String title;
    private SemanticVersion gitoolsVersion;

    public GeneSet() {
        super();
    }

    @Override
    public boolean isChanged() {
        return true;
    }

    public IResourceLocator getLocator() {
        return locator;
    }

    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }

    public SemanticVersion getGitoolsVersion() {
        if (gitoolsVersion == null) {
            return new SemanticVersion(SemanticVersion.OLD_VERSION);
        }
        return gitoolsVersion;
    }

    public void setGitoolsVersion(SemanticVersion gitoolsVersion) {
        this.gitoolsVersion = gitoolsVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        String oldValue = this.title;
        this.title = title;
        //firePropertyChange(PROPERTY_TITLE, oldValue, title);
    }
}
