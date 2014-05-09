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
package org.gitools.matrix.model.mtabixmatrix;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.upf.bg.mtabix.MTabixBlock;
import edu.upf.bg.mtabix.MTabixIndex;
import static edu.upf.bg.mtabix.bgz.BlockCompressedFilePointerUtil.getBlockAddress;
import edu.upf.bg.mtabix.bgz.BlockCompressedInputStream;
import edu.upf.bg.mtabix.bgz.BlockCompressedStreamConstants;
import edu.upf.bg.mtabix.parsers.IKeyParser;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.utils.MemoryUtils;
import org.gitools.utils.translators.DoubleTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MTabixMatrix extends HashMatrix {
    private static final Logger LOGGER = LoggerFactory.getLogger(MTabixMatrix.class);

    private MTabixIndex index;
    private BlockCompressedInputStream dataStream;
    private Set<String> indexedLayers;

    // Cache
    private Map<String, LoadingCache<Long, MTabixBlockValues>> indexedCache;

    public MTabixMatrix(MTabixIndex index, final MatrixLayers<? extends IMatrixLayer> layers, final MatrixDimensionKey... dimensions) throws IOException {
        super(layers, createMTabixDimensions(index, dimensions));

        this.index = index;
        this.dataStream = new BlockCompressedInputStream(index.getConfig().getDataFile());

        // Create the caches
        indexedCache = new HashMap<>(layers.size());
        this.indexedLayers = new HashSet<>(layers.size());
        for (int l=0; l < layers.size(); l++) {
            IMatrixLayer layer = layers.get(l);
            indexedLayers.add(layer.getId());
            indexedCache.put(layer.getId(), CacheBuilder.newBuilder().build(new BlockLoader(l)));
        }

    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {

        if (indexedLayers.contains(layer.getId())) {
            MTabixBlock block = index.getBlock(identifiers);
            if (block != null) {
                MTabixBlockValues matrixBlock = indexedCache.get(layer.getId()).getUnchecked(block.getFilePointer());
                return (T) matrixBlock.get(identifiers);
            }
        }

        T value = super.get(layer, identifiers);
        return value;
    }

    @Override
    public <T> void set(IMatrixLayer<T> layer, T value, String... identifiers) {

        if (indexedLayers.contains(layer.getId())) {
            throw new UnsupportedOperationException("The layer '" + layer.getId() + "' is read only because you are using a indexed matrix.");
        }

        super.set(layer, value, identifiers);
    }

    public void detach() {

        //TODO improve evict policy
        for (LoadingCache<Long, MTabixBlockValues> cache : indexedCache.values()) {
            cache.invalidateAll();
        }

    }

    private static IMatrixDimension[] createMTabixDimensions(MTabixIndex index, MatrixDimensionKey[] dimensions) {
        HashMatrixDimension[] result = new HashMatrixDimension[dimensions.length];

        int[] keys = index.getConfig().getKeyParser().getKeys();
        for (int k=0; k < keys.length; k++) {
            result[keys[k]] = new HashMatrixDimension(dimensions[keys[k]], index.getIndexIdentifiers().get(k));
        }

        return result;
    }


    private class BlockLoader extends CacheLoader<Long, MTabixBlockValues> {

        private int column;

        private BlockLoader(int layer) {
            this.column = layer + 2;
        }

        public MTabixBlockValues load(Long filePointer) throws IOException {

            long block = getBlockAddress(filePointer);
            LOGGER.info("Loading block '" + Long.toHexString(block) + "' column " + column);
            MTabixBlockValues matrix = new MTabixBlockValues();
            dataStream.seek(filePointer);
            IKeyParser parser = index.getConfig().getKeyParser();

            // read body
            String line;
            while ((getBlockAddress(dataStream.getFilePointer()) == block) && ((line = dataStream.readLine()) != null)) {
                final String columnId = parser.parse(line, 0);
                final String rowId = parser.parse(line, 1);
                final Double value = DoubleTranslator.get().stringToValue(parser.parse(line, column));
                matrix.set(value, rowId, columnId);
            }

            return matrix;
        }

    }


}
