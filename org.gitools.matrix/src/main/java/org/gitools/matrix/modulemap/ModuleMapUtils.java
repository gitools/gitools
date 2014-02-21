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
package org.gitools.matrix.modulemap;

import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.matrix.model.MatrixLayer;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class ModuleMapUtils {

    /**
     * Returns a new module map that only contains the modules with a total number of items between minSize and maxSize (both included)
     *
     * @param srcModuleMap the source module map
     * @param minSize      the min size
     * @param maxSize      the max size
     * @return the new module map
     */
    public static IModuleMap filterByModuleSize(IModuleMap srcModuleMap, int minSize, int maxSize) {

        HashModuleMap dstModuleMap = new HashModuleMap();

        for (String module : srcModuleMap.getModules()) {
            Collection<String> items = srcModuleMap.getMappingItems(module);

            if (minSize <= items.size() && items.size() <= maxSize) {

                for (String item : items) {
                    dstModuleMap.addMapping(module, item);
                }
            }

        }


        return dstModuleMap;
    }

    /**
     * Returns a new module map removing all items no present in 'items'
     *
     * @param srcModuleMap the src module map
     * @param items        the items
     * @return the i module map
     */
    public static IModuleMap filterByItems(IModuleMap srcModuleMap, Iterable<String> items) {

        HashModuleMap dstModuleMap = new HashModuleMap();

        Set<String> validItems = new HashSet<>();
        for (String item : items) {
            validItems.add(item);
        }

        for (String module : srcModuleMap.getModules()) {
            for (String item : srcModuleMap.getMappingItems(module)) {
                if (validItems.contains(item)) {
                    dstModuleMap.addMapping(module, item);
                }
            }
        }

        return dstModuleMap;
    }

    public static IMatrix convertToMatrix(IModuleMap moduleMap) {

        MatrixLayer<Double> layer = new MatrixLayer<>("value", Double.class);
        HashMatrix matrix = new HashMatrix(
                new MatrixLayers<MatrixLayer>(layer),
                new HashMatrixDimension(ROWS, moduleMap.getItems()),
                new HashMatrixDimension(COLUMNS, moduleMap.getModules())
        );

        for (String module : moduleMap.getModules()) {
            for (String item : moduleMap.getMappingItems(module)) {
                matrix.set(layer, 1.0, item, module);
            }
        }

        return matrix;

    }


}
