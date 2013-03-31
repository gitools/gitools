package org.gitools.kegg.service;

import org.gitools.utils.csv.CSVReader;
import org.gitools.kegg.service.domain.IdConversion;
import org.gitools.kegg.service.domain.KeggOrganism;
import org.gitools.kegg.service.domain.KeggPathway;

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
    public List<KeggOrganism> getOrganisms() throws IOException {

        CSVReader reader = createReader( REST_SERVER + "/list/organism");

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
     * @param sourceDatabase    Source database
     * @param targetDatabase    Target database
     * @return                  List of Ids relations.
     * @throws IOException
     */
    public List<IdConversion> getConvert(String sourceDatabase, String targetDatabase) throws IOException {

        CSVReader reader = createReader( REST_SERVER + "/conv/"+ targetDatabase +"/"+ sourceDatabase);

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
     * @param organismId    Target organism identifier
     * @return              List of KEGG pathways
     * @throws IOException
     */
    public List<KeggPathway> getPathways(String organismId) throws IOException {

        CSVReader reader = createReader( REST_SERVER + "/list/pathway/" + organismId);

        String fields[];
        List<KeggPathway> result = new ArrayList<KeggPathway>();
        while ((fields = reader.readNext()) != null) {
            result.add(new KeggPathway(fields));
        }

        return result;

    }

    /**
     *
     * Get the genes of a given pathway
     *
     * @param pathwayId     Pathway identifier
     * @return              List of genes within the pathway
     * @throws IOException
     */
    public List<String> getGenesByPathway(String pathwayId) throws IOException {

        CSVReader reader = createReader( REST_SERVER + "/link/genes/" + pathwayId);

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
     * @param url       The target URL
     * @return          A CSVReader to read the input as a tab-separated file
     * @throws IOException
     */
    private static CSVReader createReader(String url) throws IOException {
        URL restUrl = new URL(url);
        return new CSVReader(new BufferedReader( new InputStreamReader(restUrl.openStream())));
    }
}
