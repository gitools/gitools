/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.persistence.formats.text;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.matrix.model.DoubleBinaryMatrix;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceException;

public class DoubleBinaryMatrixFormat extends AbstractTextMatrixFormat<DoubleBinaryMatrix> {

    public DoubleBinaryMatrixFormat() {
        super(FileSuffixes.DOUBLE_BINARY_MATRIX, MimeTypes.DOUBLE_BINARY_MATRIX, DoubleBinaryMatrix.class);
    }

    @Override
    protected DoubleBinaryMatrix createEntity() {
        return new DoubleBinaryMatrix();
    }

    @Override
    protected DoubleBinaryMatrix readResource(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        return read(resourceLocator, new DoubleTranslator(), progressMonitor);
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, DoubleBinaryMatrix resource, IProgressMonitor progressMonitor) throws PersistenceException {
        write(resourceLocator, resource, new DoubleTranslator(), progressMonitor);
    }

}
