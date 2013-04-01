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
package org.gitools.persistence;

import org.gitools.persistence._DEPRECATED.PersistenceUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PersistenceUtilsTest
{

    @Test
    public void baseName()
    {
        String baseName = PersistenceUtils.getFileName("/path/file.ext1.ext2");
        assertEquals("file.ext1", baseName);
    }

    @Test
    public void forwardRelativePath()
    {
        String basePath = "/base";
        String targetPath = "/base/target/file";
        String relPath = PersistenceUtils.getRelativePath(basePath, targetPath);
        assertEquals("target/file", relPath);
    }

    @Test
    public void backwardRelativePath()
    {
        String basePath = "/base/path";
        String targetPath = "/base/file";
        String relPath = PersistenceUtils.getRelativePath(basePath, targetPath);
        assertEquals("../file", relPath);
    }
}