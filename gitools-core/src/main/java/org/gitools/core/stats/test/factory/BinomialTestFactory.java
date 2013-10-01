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
package org.gitools.core.stats.test.factory;

import org.gitools.core.model.ToolConfig;
import org.gitools.core.stats.test.BinomialTest;
import org.gitools.core.stats.test.BinomialTest.AproximationMode;
import org.gitools.core.stats.test.Test;
import org.jetbrains.annotations.NotNull;

public class BinomialTestFactory extends TestFactory {

    public static final String APROXIMATION_PROPERTY = "aproximation";

    public static final String EXACT_APROX = "exact";
    public static final String NORMAL_APROX = "normal";
    public static final String POISSON_APROX = "poisson";
    public static final String AUTOMATIC_APROX = "automatic";

    private final AproximationMode aproxMode;

    public BinomialTestFactory(@NotNull ToolConfig config) {
        super(config);

        final String aproxModeName = config.getConfiguration().get(APROXIMATION_PROPERTY);

        if ("exact".equalsIgnoreCase(aproxModeName)) {
            this.aproxMode = AproximationMode.onlyExact;
        } else if ("normal".equalsIgnoreCase(aproxModeName)) {
            this.aproxMode = AproximationMode.onlyNormal;
        } else if ("poisson".equalsIgnoreCase(aproxModeName)) {
            this.aproxMode = AproximationMode.onlyPoisson;
        } else if ("automatic".equalsIgnoreCase(aproxModeName)) {
            this.aproxMode = AproximationMode.automatic;
        } else {
            this.aproxMode = AproximationMode.onlyExact;
        }
    }

	/*public BinomialTestFactory(AproximationMode aproxMode) {
        this.aproxMode = aproxMode;
	}*/

    @NotNull
    @Override
    public Test create() {
        return new BinomialTest(aproxMode);
    }

}
