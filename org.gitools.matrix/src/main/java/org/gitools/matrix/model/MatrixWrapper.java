/*
 * #%L
 * org.gitools.matrix
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.matrix.model;

import org.gitools.api.matrix.*;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.api.resource.SemanticVersion;

public abstract class MatrixWrapper implements IMatrix {

    private IMatrix wrapMatrix;

    public MatrixWrapper(IMatrix wrapMatrix) {
        this.wrapMatrix = wrapMatrix;
    }

    @Override
    public MatrixDimensionKey[] getDimensionKeys() {
        return wrapMatrix.getDimensionKeys();
    }

    @Override
    public IMatrixDimension getDimension(MatrixDimensionKey dimension) {
        return wrapMatrix.getDimension(dimension);
    }

    @Override
    public IMatrixLayers<? extends IMatrixLayer> getLayers() {
        return wrapMatrix.getLayers();
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, IMatrixPosition position) {
        return wrapMatrix.get(layer, position);
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {
        return wrapMatrix.get(layer, identifiers);
    }

    @Override
    public <T> void set(IMatrixLayer<T> layer, T value, IMatrixPosition position) {
        wrapMatrix.set(layer, value, position);
    }

    @Override
    public <T> void set(IMatrixLayer<T> layer, T value, String... identifiers) {
        wrapMatrix.set(layer, value, identifiers);
    }

    @Override
    public IMatrixPosition newPosition() {
        return wrapMatrix.newPosition();
    }

    @Override
    public IMatrix subset(IMatrixDimension... dimensionSubsets) {
        return wrapMatrix.subset(dimensionSubsets);
    }

    @Override
    public void detach(IMatrixLayer topLayer) {
        wrapMatrix.detach(topLayer);
    }

    @Override
    public <T> void setMetadata(IKey<T> key, T value) {
        wrapMatrix.setMetadata(key, value);
    }

    @Override
    public <T> T getMetadata(IKey<T> key) {
        return wrapMatrix.getMetadata(key);
    }

    @Override
    public IMatrixDimension getRows() {
        return wrapMatrix.getRows();
    }

    @Override
    public IMatrixDimension getColumns() {
        return wrapMatrix.getColumns();
    }

    @Override
    public boolean isChanged() {
        return wrapMatrix.isChanged();
    }

    @Override
    public IResourceLocator getLocator() {
        return wrapMatrix.getLocator();
    }

    @Override
    public void setLocator(IResourceLocator locator) {
        wrapMatrix.setLocator(locator);
    }

    @Override
    public void setGitoolsVersion(SemanticVersion v) {

    }

    @Override
    public SemanticVersion getGitoolsVersion() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void setTitle(String title) {

    }
}
