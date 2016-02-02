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
import edu.upf.bg.mtabix.MTabixConfig;
import edu.upf.bg.mtabix.MTabixIndex;
import edu.upf.bg.mtabix.compress.BlockCompressedReader;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.matrix.model.MatrixLayers;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.utils.translators.DoubleTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static edu.upf.bg.mtabix.compress.BlockCompressedFilePointerUtil.getBlockAddress;

public class MTabixMatrix extends HashMatrix {
    private static final Logger LOGGER = LoggerFactory.getLogger(MTabixMatrix.class);

    private MTabixIndex index;
    private BlockCompressedReader dataStream;
    private Set<String> indexedLayers;

    // Cache
    private Map<String, LoadingCache<Long, MTabixBlockValues>> indexedCache;

    public MTabixMatrix(MTabixIndex index, final MatrixLayers<? extends IMatrixLayer> layers, final MatrixDimensionKey... dimensions) throws IOException {
        super(layers, createMTabixDimensions(index, dimensions));

        this.index = index;
        this.dataStream = new BlockCompressedReader(index.getConfig().getDataFile());

        // Create the caches
        indexedCache = new HashMap<>(layers.size());
        this.indexedLayers = new HashSet<>(layers.size());
        for (int l=0; l < layers.size(); l++) {
            IMatrixLayer layer = layers.get(l);
            indexedLayers.add(layer.getId());
            indexedCache.put(layer.getId(), CacheBuilder.newBuilder().build(new BlockLoader(l)));
        }

        hasChanged = false;
    }

    @Override
    public <T> T get(IMatrixLayer<T> layer, String... identifiers) {

        if (indexedLayers.contains(layer.getId())) {
            try {

                MTabixBlock block = index.getBlock(identifiers);

                if (block != null) {
                    MTabixBlockValues matrixBlock = indexedCache.get(layer.getId()).getUnchecked(block.getFilePointer());
                    return (T) matrixBlock.get(identifiers);
                }

            } catch (NoSuchElementException e) {
                return null;
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
        MTabixMatrixDimension[] result = new MTabixMatrixDimension[dimensions.length];

        MTabixConfig config = index.getConfig();
        int[] keys = config.getKeyParser().getKeys();
        for (int k=0; k < keys.length; k++) {
            result[keys[k]] = new MTabixMatrixDimension(dimensions[keys[k]], index.getIndexIdentifiers().get(k));
        }

        return result;
    }

    private int lineLength;
    private byte[] line = new byte[8192];

    private synchronized MTabixBlockValues loadBlock(Long filePointer, int column) throws IOException {

        long block = getBlockAddress(filePointer);
        LOGGER.info("Loading block '" + Long.toHexString(block) + "' column " + column);
        MTabixBlockValues matrix = new MTabixBlockValues(this.index.getConfig().getKeyParser(), this.index.getIndexIdentifiers());
        dataStream.seek(filePointer);

        while ((getBlockAddress(dataStream.getFilePointer()) == block) && ((lineLength = dataStream.readLine(line)) != -1)) {

            final Double value = DoubleTranslator.get().stringToValue(parseLine(line, column, lineLength));
            if (value!=null) {
                String id1 = parseLine(line, 1, lineLength);
                String id0 = parseLine(line, 0, lineLength);
                long key = matrix.hash(id1, id0);
                matrix.put(key, value);
            }
        }

        return matrix;

    }

    private int start, end, tabs;
    private int parseTab(byte[] str, int pos, int length) {

        if (length < 1) {
            return -1;
        }

        tabs=pos; start=0; end=0;

        while(true) {
            if (str[end] == '\t') {

                if (tabs == 0) {
                    break;
                }

                tabs--;
                start = end + 1;
            }

            end++;
            if (end == length) {
                if (tabs == pos) {
                    return -1;
                }
                break;
            }

        }

        if (start != end && str[start] == '"' && str[end-1] == '"') {
            start = start + 1;
            end = end - 1;
        }

        return end - start;

    }

    public String parseLine(byte[] str, int pos, int length) {

        int size = parseTab(str, pos, length);

        if (size == -1) {
            return null;
        }

        return new String(str, start, size);
    }

    private class BlockLoader extends CacheLoader<Long, MTabixBlockValues> {

        private int column;
        private BlockLoader(int layer) {
            this.column = layer + 2;
        }

        public MTabixBlockValues load(Long filePointer) throws IOException {
             return loadBlock(filePointer, column);
        }

    }

}
