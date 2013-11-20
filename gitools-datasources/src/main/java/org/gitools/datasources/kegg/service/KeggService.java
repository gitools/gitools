/*
 * #%L
 * gitools-kegg
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
package org.gitools.datasources.kegg.service;

import org.gitools.datasources.kegg.service.domain.IdConversion;
import org.gitools.datasources.kegg.service.domain.KeggOrganism;
import org.gitools.datasources.kegg.service.domain.KeggPathway;
import org.gitools.utils.csv.CSVReader;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This service interact with the KEGG REST API
 */
public class KeggService {

    private final static String REST_SERVER = "http://rest.kegg.jp";

    /**
     * @return The list of available organism
     * @throws IOException
     */
    @NotNull
    public List<KeggOrganism> getOrganisms() throws IOException {

        CSVReader reader = createReader(REST_SERVER + "/list/organism");

        String fields[];
        List<KeggOrganism> result = new ArrayList<KeggOrganism>();
        while ((fields = reader.readNext()) != null) {
            result.add(new KeggOrganism(fields));
        }

        return result;
    }

    /**
     * List of Identifiers conversion
     *
     * @param sourceDatabase Source database
     * @param targetDatabase Target database
     * @return List of Ids relations.
     * @throws IOException
     */
    @NotNull
    public List<IdConversion> getConvert(String sourceDatabase, String targetDatabase) throws IOException {

        CSVReader reader = createReader(REST_SERVER + "/conv/" + targetDatabase + "/" + sourceDatabase);

        String fields[];
        List<IdConversion> result = new ArrayList<IdConversion>();
        while ((fields = reader.readNext()) != null) {
            result.add(new IdConversion(fields));
        }

        return result;

    }

    /**
     * Get all the pathways of one organism
     *
     * @param organismId Target organism identifier
     * @return List of KEGG pathways
     * @throws IOException
     */
    @NotNull
    public List<KeggPathway> getPathways(String organismId) throws IOException {

        CSVReader reader = createReader(REST_SERVER + "/list/pathway/" + organismId);

        String fields[];
        List<KeggPathway> result = new ArrayList<KeggPathway>();
        while ((fields = reader.readNext()) != null) {
            result.add(new KeggPathway(fields));
        }

        return result;

    }

    /**
     * Get the genes of a given pathway
     *
     * @param pathwayId Pathway identifier
     * @return List of genes within the pathway
     * @throws IOException
     */
    @NotNull
    public List<String> getGenesByPathway(String pathwayId) throws IOException {

        CSVReader reader = createReader(REST_SERVER + "/link/genes/" + pathwayId);

        String fields[];
        List<String> result = new ArrayList<String>();
        while ((fields = reader.readNext()) != null) {
            result.add(fields[1]);
        }

        return result;

    }

    /**
     * Simple function to open a URL and read it's content as a tab-separated file.
     *
     * @param url The target URL
     * @return A CSVReader to read the input as a tab-separated file
     * @throws IOException
     */
    @NotNull
    private static CSVReader createReader(String url) throws IOException {
        URL restUrl = new URL(url);
        return new CSVReader(new BufferedReader(new InputStreamReader(restUrl.openStream())));
    }
}
