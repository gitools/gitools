/*
 * #%L
 * org.gitools.utils
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
package org.gitools.utils.readers.profile;


import org.gitools.utils.readers.MatrixReaderProfile;

import java.util.List;

public class DefaultFlatTextReaderProfiles {

    /**
     * This class contains profiles for known flat text data format
     */


    private static List<ReaderProfile> defaultProfiles;

    public static List<ReaderProfile> getDefaultProfiles() {
        if (defaultProfiles == null) {
            init();
        }
        return defaultProfiles;
    }

    private static void init() {
        //TDM
        TableReaderProfile tdm = new TableReaderProfile();
        tdm.setName("tdm");
        tdm.setHeatmapColumnsIds(new int[0]);
        tdm.setHeatmapRowsIds(new int[1]);
        defaultProfiles.add(tdm);

        //CDM
        MatrixReaderProfile cdm = new MatrixReaderProfile();
        cdm.setName("cdm");
        cdm.setColumnIdsPosition(0);
        cdm.setRowIdsPosition(0);
        defaultProfiles.add(cdm);

        //BDM
        MatrixReaderProfile bdm = new MatrixReaderProfile();
        bdm.setName("bdm");
        bdm.setColumnIdsPosition(0);
        cdm.setRowIdsPosition(0);
        defaultProfiles.add(bdm);

        //GCT
        MatrixReaderProfile gct = new MatrixReaderProfile();
        gct.setName("gct");
        gct.setColumnIdsPosition(0);
        gct.setRowIdsPosition(0);
        gct.setIgnoredColumns(new int[1]);
        defaultProfiles.add(gct);


    }

}
