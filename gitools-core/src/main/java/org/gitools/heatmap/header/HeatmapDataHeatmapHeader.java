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

package org.gitools.heatmap.header;

import edu.upf.bg.xml.adapter.ColorXmlAdapter;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

public class HeatmapDataHeatmapHeader extends HeatmapHeader {

    private static final String HEADER_HEATMAP_CHANGED = "headerHeatmap";
    private Heatmap headerHeatmap;

    public HeatmapDataHeatmapHeader(HeatmapDim hdim) {
		super(hdim);
		
		size = 20;

        labelColor = Color.BLACK;

	}

    public void setHeaderHeatmap (Heatmap headerHeatmap) {
        Heatmap old = this.headerHeatmap;
        this.headerHeatmap = headerHeatmap;
        firePropertyChange(HEADER_HEATMAP_CHANGED,old,headerHeatmap);
    }

    public Heatmap getHeaderHeatmap () {
        return this.headerHeatmap;
    }

    @Override
    public String getTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("Data: ");
        sb.append(headerHeatmap.getTitle());
        return sb.toString();
    }

}
