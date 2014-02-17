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
