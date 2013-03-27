package org.gitools.model;

import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;

import java.util.ArrayList;

public class GeneSet extends ArrayList<String> implements IResource {

    private IResourceLocator locator;

    public GeneSet() {
        super();
    }

    public IResourceLocator getLocator() {
        return locator;
    }

    public void setLocator(IResourceLocator locator) {
        this.locator = locator;
    }
}
