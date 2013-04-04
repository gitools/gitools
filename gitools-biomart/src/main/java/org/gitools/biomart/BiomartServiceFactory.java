/*
 * #%L
 * gitools-biomart
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
package org.gitools.biomart;

import org.gitools.biomart.restful.BiomartRestfulService;
import org.gitools.biomart.settings.BiomartSource;
import org.jetbrains.annotations.NotNull;

public class BiomartServiceFactory
{

    private BiomartServiceFactory instance;

    public BiomartServiceFactory getInstance()
    {
        if (instance == null)
        {
            instance = new BiomartServiceFactory();
        }

        return instance;
    }

    private BiomartServiceFactory()
    {
    }

    /**
     * Creates a Biomart service from a Biomart source
     *
     * @param source
     * @return biomart service
     */
    @NotNull
    public static BiomartService createService(BiomartSource source) throws BiomartServiceException
    {
        BiomartService bs = new BiomartRestfulService(source);
        return bs;
    }
}
