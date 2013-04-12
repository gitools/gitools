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

/**
 * A compress row contains a compressed buffer with all the
 * values of a matrix row.
 */
public class CompressRow
{
    private int length;
    private byte[] content;

    /**
     * Instantiates a new Compress row.
     *
     * @param length the length of the content when it is expanded.
     * @param content the content is a buffer with all the compressed values
     */
    public CompressRow(int length, byte[] content)
    {
        this.length = length;
        this.content = content;
    }

    /**
     * Get the compressed content of this row.
     *
     * @return the byte [ ]
     */
    public byte[] getContent()
    {
        return content;
    }

    /**
     * Length of the content when it is expanded.
     *
     * @return the length
     */
    public int getNotCompressedLength()
    {
        return length;
    }

}
