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
package org.gitools.matrix.model.compressmatrix;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.matrix.model.IMatrixLayer;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.matrix.model.SimpleMatrixLayers;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.formats.compressmatrix.CompressedMatrixFormat;
import org.gitools.utils.MemoryUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

/**
 * This element adapter contains all the values of a matrix and takes care
 * to expand the rows at request time.
 * <p/>
 * Keeps a dynamic cache with the expanded values. The size of the cache
 * will grow depending on the available free memory.
 */
public class CompressElementAdapter {







}
