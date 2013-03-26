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

package org.gitools.persistence.text;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.matrix.model.DoubleMatrix;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;

public class DoubleMatrixTextPersistence extends MatrixTextPersistence<DoubleMatrix> {

    public DoubleMatrixTextPersistence() {
        super();
    }

    public DoubleMatrixTextPersistence(int skipLines, Integer... skipColumns) {
        super(skipLines, skipColumns);
    }

    @Override
    protected DoubleMatrix createEntity() {
        return new DoubleMatrix();
    }

    @Override
    public DoubleMatrix read(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        return read(resourceLocator, getValueTranslator(), progressMonitor);
    }

    @Override
    public void write(IResourceLocator resourceLocator, DoubleMatrix doubleMatrix, IProgressMonitor progressMonitor) throws PersistenceException {
        write(resourceLocator, doubleMatrix, getValueTranslator(), progressMonitor);
    }
}
