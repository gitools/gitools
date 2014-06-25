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
package org.gitools.ui.platform.icons;

import org.gitools.api.matrix.MatrixDimensionKey;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;
import static org.gitools.api.matrix.MatrixDimensionKey.ROWS;

public class IconNames {

    private static final Map<MatrixDimensionKey, DimensionIcons> dimensionIcons = new HashMap<>();

    public static DimensionIcons get(MatrixDimensionKey key) {
        return dimensionIcons.get(key);
    }

    static {
        dimensionIcons.put(ROWS, new IconNamesRows());
        dimensionIcons.put(COLUMNS, new IconNamesColumns());
    }

    public static final String empty16 = "/img/empty16.png";

    public static final String info = "/img/info.png";
    public static final String fullscreen = "/img/fullscreen.png";

    public static final String logoNoText = "/img/LogoNoText.png";
    public static final String logoMini = "/img/LogoMini.png";
    public static final String aboutLogo = "/img/AboutLogo.png";
    public static final String logo16 = "/img/logo16.png";
    public static final String logo24 = "/img/logo24.png";

    public static final String open16 = "/img/Open16.png";
    public static final String open24 = "/img/Open24.png";

    public static final String openAnalysis16 = "/img/OpenAnalysis16.png";
    public static final String openAnalysis24 = "/img/OpenAnalysis24.png";

    public static final String heatmap16 = "/img/Heatmap16.png";
    public static final String heatmap24 = "/img/Heatmap24.png";

    public static final String analysisHeatmap16 = "/img/Analysis16.png";
    public static final String analysisHeatmap26 = "/img/Analysis24.png";

    public static final String openMatrix16 = "/img/OpenMatrix16.png";
    public static final String openMatrix24 = "/img/OpenMatrix24.png";

    public static final String edit16 = "/img/edit16.png";
    public static final String editHeatmap16 = "/img/editHeatmap16.png";

    public static final String settings16 = "/img/settings16.png";
    public static final String shortcuts16 = "/img/shortcuts16.png";

    public static final String moveUp16 = "/img/moveUp16.png";
    public static final String moveDown16 = "/img/moveDown16.png";
    public static final String nextEventRight16 = "/img/nextEventRight16.png";
    public static final String nextEventLeft16 = "/img/nextEventLeft16.png";
    public static final String nextEventDown16 = "/img/nextEventDown16.png";

    public static final String add16 = "/img/add16.png";
    public static final String remove16 = "/img/remove16.png";


    public static final String close16 = "/img/close16.png";
    public static final String close24 = "/img/close24.png";

    public static final String save16 = "/img/Save16.gif";
    public static final String save24 = "/img/Save24.gif";

    public static final String selectAll16 = "/img/SelectAll16.gif";
    public static final String selectAll24 = "/img/SelectAll24.gif";

    public static final String sortSelectedColumns16Asc = "/img/SortSelectedColumns16Asc.gif";
    public static final String sortSelectedColumns24Asc = "/img/SortSelectedColumns24Asc.gif";

    public static final String sortSelectedColumns16Desc = "/img/SortSelectedColumns16Asc.gif";
    public static final String sortSelectedColumns24Desc = "/img/SortSelectedColumns24Desc.gif";

    public static final String unselectAll16 = "/img/UnselectAll16.gif";
    public static final String unselectAll24 = "/img/UnselectAll24.gif";

    public static final String chain24 = "/img/Chain.png";

    public static final String newDataHeatmap16 = "/img/NewDataHeatmap16.gif";
    public static final String newDataHeatmap24 = "/img/NewDataHeatmap24.gif";

    public static final String newResultsHeatmap16 = "/img/NewResultsHeatmap16.gif";
    public static final String newResultsHeatmap24 = "/img/NewResultsHeatmap24.gif";

    public static final String cloneHeatmap24 = "/img/CloneHeatmap24.gif";

    public static final String viewAnnotatedElements16 = "/img/ViewAnnotatedElements16.gif";
    public static final String viewAnnotatedElements24 = "/img/ViewAnnotatedElements24.gif";

    public static final String view16 = "/img/view16.png";
    public static final String view24 = "/img/view24.png";

    public static final String biomart16 = "/img/Biomart16.png";
    public static final String biomart24 = "/img/Biomart24.png";

    public static final String intogen16 = "/img/Intogen16.png";
    public static final String intogen24 = "/img/Intogen24.png";

    public static final String excel16 = "/img/excel16.png";
    public static final String excel24 = "/img/excel24.png";
    public static final String excel48 = "/img/excel48.png";

    public static final String gs16 = "/img/gs16.png";
    public static final String gs24 = "/img/gs24.png";
    public static final String gs48 = "/img/gs48.png";

    public static final String igv16 = "/img/igv16.png";
    public static final String igv24 = "/img/igv24.png";

    public static final String bookmark48 = "/img/bookmark48.png";
    public static final String bookmark24 = "/img/bookmark24.png";
    public static final String bookmark16 = "/img/bookmark16.png";
    public static final String bookmarkAdd48 = "/img/bookmark_add48.png";
    public static final String bookmarkAdd24 = "/img/bookmark_add24.png";
    public static final String bookmarkAdd16 = "/img/bookmark_add16.png";


    public static final String zoomIn16 = "/img/Zoom-In16.png";
    public static final String zoomOut16 = "/img/Zoom-Out16.png";

    public static final String KEGG16 = "/img/Kegg16.png";
    public static final String KEGG24 = "/img/Kegg24.png";

    public static final String GO16 = "/img/GO16.png";
    public static final String GO24 = "/img/GO24.png";

    public static final String DOWNLOAD = "/img/Download.png";

    public static final String LOGO_SELECT_FILE = "/img/logos/SelectFile.png";

    public static final String LOGO_INTOGEN = "/img/intogen/IntOGenWebLogo.png";

    public static final String LOGO_INTOGEN_IMPORT = "/img/logos/IntogenImport.png";

    public static final String LOGO_BIOMART_IMPORT = "/img/logos/BiomartImport.png";

    public static final String LOGO_KEGG = "/img/Kegg128.png";

    public static final String LOGO_GO = "/img/GO128.png";

    public static final String LOGO_ENRICHMENT = "/img/logos/Enrichment.png";

    public static final String LOGO_CORRELATION = "/img/logos/Correlation.png";

    public static final String LOGO_GROUP_COMPARISON = "/img/logos/Comparison.png";

    public static final String LOGO_OVERLAPPING = "/img/logos/Overlapping.png";

    public static final String LOGO_COMBINATION = "/img/logos/Combination.png";

    public static final String LOGO_ONCODRIVE = "/img/logos/Oncodrive.png";

    public static final String LOGO_SAVE = "/img/logos/Save.png";

    public static final String LOGO_DATA = "/img/logos/Data.png";

    public static final String LOGO_MODULES = "/img/logos/Modules.png";

    public static final String LOGO_METHOD = "/img/logos/Method.png";

    public static final String LOGO_ANALYSIS_DETAILS = "/img/logos/AnalysisDetails.png";
    public static final String LOGO_ANALYSIS_DETAILS16 = "/img/logos/AnalysisDetails16.png";

    public static final String LOGO_ANALYSIS_OPEN = "/img/logos/AnalysisOpen.png";

    public static final String LOGO_MATRIX_OPEN = "/img/logos/MatrixOpen.png";


    public static final String LOGO_CLUSTERING = "/img/logos/Clustering.png";

    public static final String LOGO_ANNOTATION_TEXT_LABEL_HEADER = "/img/logos/HeaderAnnotationTextLabel.png";

    public static final String LOGO_ANNOTATION_COLORED_LABEL = "/img/logos/HeaderAnnotationColorLabel.png";

    public static final String LOGO_AGGREGATED_DATA_HEATMAP = "/img/logos/HeaderAggregatedDataHeatmap.png";

    public static final String LOGO_ANNOTATION_HEATMAP = "/img/logos/HeaderAnnotationHeatmap.png";

    public static final String LOGO_AGGREGATED_DATA_TEXT_LABEL = "/img/logos/HeaderAggregatedDataTextLabel.png";

    public static final String SEARCH16 = "/img/Search16.png";
    public static final String SEARCH24 = "/img/Search24.png";

    public static final URL ANALYSIS_IMAGE_MANN_WHITNEY_WILCOXON = IconNames.class.getResource("/img/AnalysisImageMannWhitneyWilcoxon.png");

    public static final URL DATA_FORMAT_MATRIX = IconNames.class.getResource("/img/DataFormatMatrix.png");

    public static final URL DATA_FORMAT_TABLE = IconNames.class.getResource("/img/DataFormatTable.png");


    public static final String drag = "/img/drag.png";

    public static final ImageIcon INFO_ICON = new ImageIcon(IconNames.class.getResource("/img/info.png"));

    public static final ImageIcon CREATE_IMAGE_LARGE_ICON = new ImageIcon(IconNames.class.getResource("/img/snapshot24.png"));

    public static final ImageIcon CREATE_IMAGE_SMALL_ICON = new ImageIcon(IconNames.class.getResource("/img/snapshot16.png"));
}
