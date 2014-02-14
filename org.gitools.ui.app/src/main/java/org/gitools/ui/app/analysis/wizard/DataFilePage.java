/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app.analysis.wizard;

import org.gitools.api.PersistenceException;
import org.gitools.api.persistence.FileFormat;
import org.gitools.matrix.FileFormats;
import org.gitools.matrix.format.CmatrixMatrixFormat;
import org.gitools.matrix.format.TdmMatrixFormat;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.app.settings.Settings;


public class DataFilePage extends SelectFilePage {

    private static final FileFormat[] formats = new FileFormat[]{FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED, FileFormats.DOUBLE_MATRIX, FileFormats.DOUBLE_BINARY_MATRIX, FileFormats.MULTIVALUE_DATA_MATRIX, FileFormats.COMPRESSED_MATRIX, FileFormats.MODULES_2C_MAP, FileFormats.MODULES_INDEXED_MAP};

    public DataFilePage() {
        this(formats);
    }

    public DataFilePage(FileFormat[] formats) {
        super(formats);

        setTitle("Select data source");

        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_DATA, 96));
    }

    @Override
    protected void updateState() {

        FileFormat ff = getFileFormat();
        super.updateState();

        if (isComplete() && (ff.getExtension().equals(FileFormats.MULTIVALUE_DATA_MATRIX.getExtension()))) {
            activateValueSelection();

            String[] headers = new String[0];
            try {
                headers = TdmMatrixFormat.readHeader(getFile());
            } catch (PersistenceException e) {
                setMessage(MessageStatus.ERROR, "Error reading headers of " + getFile().getName());
                setComplete(false);
                deactivateValueSelection();
            }
            setValues(headers);

            return;
        }

        if (isComplete() && (ff.getExtension().equals(FileFormats.COMPRESSED_MATRIX.getExtension()))) {
            activateValueSelection();

            String[] headers = new String[0];
            try {
                headers = CmatrixMatrixFormat.readHeader(getFile());
            } catch (PersistenceException e) {
                setMessage(MessageStatus.ERROR, "Error reading headers of " + getFile().getName());
                setComplete(false);
                deactivateValueSelection();
            }
            setValues(headers);

            return;
        }

        deactivateValueSelection();

    }

}
