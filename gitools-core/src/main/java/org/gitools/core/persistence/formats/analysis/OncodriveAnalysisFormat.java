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
package org.gitools.core.persistence.formats.analysis;

import org.gitools.core.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.core.persistence._DEPRECATED.FileFormat;

public class OncodriveAnalysisFormat extends AbstractXmlFormat<OncodriveAnalysis> {
    public static final String EXTENSION = "oncodrive";
    private static final Class<OncodriveAnalysis> RESOURCE_CLASS = OncodriveAnalysis.class;
    public static final FileFormat FILE_FORMAT = new FileFormat("Oncodrive analysis", EXTENSION);

    public OncodriveAnalysisFormat() {
        super(EXTENSION, RESOURCE_CLASS);
    }
}
