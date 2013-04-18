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
package org.gitools.biomart.test;

import org.gitools.biomart.BiomartService;
import org.gitools.biomart.BiomartServiceException;
import org.gitools.biomart.BiomartServiceFactory;
import org.gitools.biomart.restful.model.*;
import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;
import org.gitools.utils.progressmonitor.DefaultProgressMonitor;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.List;

import static org.junit.Assert.*;


/**
 * @noinspection ALL
 */
public class BiomartServiceTest {

    @Nullable
    private BiomartService bs;
    private static final Logger log = LoggerFactory.getLogger(BiomartServiceTest.class.getName());

    @Test
    public void dummyTest() {
    }

    //@Before
    public void before() {

        bs = defaultConnexionTest();

    }

    @Nullable
    private BiomartService defaultConnexionTest() {
        BiomartService srv = null;
        try {

            List<BiomartSource> lBs = BiomartSourceManager.getDefault().getSources();

            BiomartSource bsrc = lBs.get(0);
            assertNotNull(bsrc);

            srv = BiomartServiceFactory.createService(bsrc);
            assertNotNull(srv);


        } catch (BiomartServiceException ex) {
            log.error(ex.getMessage());
        }

        return srv;
    }

    /**
     * @noinspection UnusedDeclaration
     */ //@Test
    public void getRegistry() {
        try {

            List<MartLocation> lMart = bs.getRegistry();

            assertNotNull(lMart);
            assertTrue(lMart.size() > 0);

        } catch (BiomartServiceException ex) {
            log.error(ex.getMessage());
        }
    }


    //@Test
    public void getDatasets() {
        try {


            List<MartLocation> lMart = bs.getRegistry();
            assertNotNull(lMart);
            assertTrue(lMart.size() > 0);


            MartLocation m = lMart.get(0);
            List<DatasetInfo> ds = bs.getDatasets(m);
            assertNotNull(ds);
            assertTrue(ds.size() > 0);

            log.info("num DS: " + ds.size());

        } catch (BiomartServiceException ex) {
            log.error(ex.getMessage());
        }
    }


    //@Test
    public void getAttributes() {
        try {
            List<MartLocation> lMart = bs.getRegistry();
            assertNotNull(lMart);
            assertTrue(lMart.size() > 0);

            MartLocation m = lMart.get(0);

            List<DatasetInfo> ds = bs.getDatasets(m);
            assertNotNull(ds);
            assertTrue(ds.size() > 0);

            DatasetInfo d = ds.get(0);

            List<AttributePage> latt = bs.getAttributes(m, d);

            log.info("MART: " + m.getDisplayName() + "DS: " + d.getDisplayName());
            assertNotNull(latt);
            assertTrue(latt.size() > 0);
            assertTrue(latt.get(0).getAttributeGroups().size() > 0);
            assertTrue(latt.get(0).getAttributeGroups().get(0).getAttributeCollections().size() > 0);
            assertTrue(latt.get(0).getAttributeGroups().get(0).getAttributeCollections().get(0).getAttributeDescriptions().size() > 0);
            assertNotNull(latt.get(0).getAttributeGroups().get(0).getAttributeCollections().get(0).getAttributeDescriptions().get(0).getInternalName());
            assertNotSame(latt.get(0).getAttributeGroups().get(0).getAttributeCollections().get(0).getAttributeDescriptions().get(0).getInternalName(), "");

            log.info("Num Att Pages: " + latt.size());

        } catch (BiomartServiceException ex) {
            log.error(ex.getMessage());
        }
    }

    //@Test
    public void getFilters() {
        try {

            List<MartLocation> lMart = bs.getRegistry();
            assertNotNull(lMart);
            assertTrue(lMart.size() > 0);

            MartLocation m = lMart.get(0);

            List<DatasetInfo> ds = bs.getDatasets(m);
            assertNotNull(ds);
            assertTrue(ds.size() > 0);

            DatasetInfo d = ds.get(0);

            List<FilterPage> lf = bs.getFilters(m, d);

            log.info("MART: " + m.getDisplayName() + "DS: " + d.getDisplayName());
            assertNotNull(lf);
            assertTrue(lf.size() > 0);
            assertTrue(lf.get(0).getFilterGroups().size() > 0);
            assertTrue(lf.get(0).getFilterGroups().get(0).getFilterCollections().size() > 0);
            assertTrue(lf.get(0).getFilterGroups().get(0).getFilterCollections().get(0).getFilterDescriptions().size() > 0);
            assertNotNull(lf.get(0).getFilterGroups().get(0).getFilterCollections().get(0).getFilterDescriptions().get(0).getInternalName());
            assertNotSame(lf.get(0).getFilterGroups().get(0).getFilterCollections().get(0).getFilterDescriptions().get(0).getInternalName(), "");

            log.info("Num Filter Pages: " + lf.size());
            log.info("Num Filter Groups: " + lf.get(0).getFilterGroups().size());


        } catch (BiomartServiceException ex) {
            log.error(ex.getMessage());
        }
    }

    //@Test
    public void QueryAsStream() throws JAXBException {
        try {
            InputStream in = null;
            BufferedReader br = null;

            List<MartLocation> lMart = bs.getRegistry();
            assertNotNull(lMart);
            assertTrue(lMart.size() > 0);

            MartLocation mart = lMart.get(0);

            List<DatasetInfo> lDs = bs.getDatasets(mart);
            assertNotNull(lDs);
            assertTrue(lDs.size() > 0);

            DatasetInfo d = lDs.get(0);

            log.info("MART: " + mart.getDisplayName() + "DS: " + d.getDisplayName());

            List<FilterPage> lf = bs.getFilters(mart, d);
            List<AttributePage> dsattrs = bs.getAttributes(mart, d);

            Query query = createSimpleQuery(lf, dsattrs, mart, d);
/*
            JAXBContext context = JAXBContext.newInstance(Query.class);
			Marshaller m = context.createMarshaller();
			m.marshal(query, System.out);
			System.out.println();
*/
            in = bs.queryAsStream(query, "TSV");
            assertNotNull(in);
            br = new BufferedReader(new InputStreamReader(in));
            assertNotNull(br);

            String res = null;
            Integer rows = 0;
            while ((res = br.readLine()) != null) {
                rows++;
            }
            log.info("NumRows: " + rows);
            assertTrue(rows > 0);

        } catch (IOException ex) {
            log.error(ex.getMessage());
        } catch (BiomartServiceException ex) {
            log.error(ex.getMessage());
        }

    }

    @NotNull
    private Query createSimpleQuery(@NotNull List<FilterPage> lf, @NotNull List<AttributePage> dsattrs, @NotNull MartLocation mart, @NotNull DatasetInfo d) throws BiomartServiceException {

        Attribute a = new Attribute();
        a.setName(dsattrs.get(0).getAttributeGroups().get(0).getAttributeCollections().get(0).getAttributeDescriptions().get(0).getInternalName());

        Attribute a1 = new Attribute();
        a1.setName(dsattrs.get(0).getAttributeGroups().get(0).getAttributeCollections().get(0).getAttributeDescriptions().get(1).getInternalName());

        Filter f = new Filter();
        f.setName(lf.get(0).getFilterGroups().get(0).getFilterCollections().get(0).getFilterDescriptions().get(0).getInternalName());
        f.setValue("1");

        log.info("ATT: " + a.getName() + "," + a1.getName() + " FILTER: " + f.getName() + " VAL:" + f.getValue());
        log.info("VirtualSchema: " + mart.getServerVirtualSchema());

        Dataset ds = new Dataset();
        ds.setName(d.getName());
        ds.getFilter().add(f);
        ds.getAttribute().add(a);
        ds.getAttribute().add(a1);

        Query query = new Query();
        query.setVirtualSchemaName(mart.getServerVirtualSchema());
        query.setHeader(1);
        query.setCount(0);
        query.setUniqueRows(1);
        query.getDatasets().add(ds);

        return query;
    }

    //@Test
    public void queryModule() {

        try {

            final File file = new File(System.getProperty("user.home", ".") + File.separator + "testQuery.test");
            String format = "TSV";
            IProgressMonitor monitor = new DefaultProgressMonitor();

            List<MartLocation> lMart = bs.getRegistry();
            assertNotNull(lMart);
            assertTrue(lMart.size() > 0);

            MartLocation mart = lMart.get(0);
            List<DatasetInfo> lDs = bs.getDatasets(mart);
            assertNotNull(lDs);
            assertTrue(lDs.size() > 0);

            DatasetInfo d = lDs.get(0);

            List<FilterPage> lf = bs.getFilters(mart, d);
            List<AttributePage> dsattrs = bs.getAttributes(mart, d);

            Query query = createSimpleQuery(lf, dsattrs, mart, d);

            bs.queryModule(query, file, format, monitor);
            log.info("File lenght: " + file.length());
            assertTrue(file.length() > 0);
            file.delete();

        } catch (BiomartServiceException ex) {
            log.error(ex.getMessage());
        }

    }

    //@Test
    public void queryTable() {
        try {
            final File file = new File(System.getProperty("user.home", ".") + File.separator + "testQuery.test");
            String format = "TSV";
            IProgressMonitor monitor = new DefaultProgressMonitor();

            List<MartLocation> lMart = bs.getRegistry();
            assertNotNull(lMart);
            assertTrue(lMart.size() > 0);
            MartLocation mart = lMart.get(0);
            List<DatasetInfo> lDs = bs.getDatasets(mart);
            assertNotNull(lDs);
            assertTrue(lDs.size() > 0);
            DatasetInfo d = lDs.get(0);
            List<FilterPage> lf = bs.getFilters(mart, d);
            List<AttributePage> dsattrs = bs.getAttributes(mart, d);
            Query query = createSimpleQuery(lf, dsattrs, mart, d);

            bs.queryTable(query, file, format, true, "", monitor);

            log.info("File lenght: " + file.length());
            assertTrue(file.length() > 0);
            file.delete();


        } catch (BiomartServiceException ex) {
            log.error(ex.getMessage());
        }
    }

    //@Test
    public void getDatasetConfig() throws IOException, JAXBException {

        try {

            List<MartLocation> lMart = bs.getRegistry();
            assertNotNull(lMart);
            assertTrue(lMart.size() > 0);
            MartLocation mart = lMart.get(0);
            List<DatasetInfo> lDs = bs.getDatasets(mart);
            assertNotNull(lDs);
            assertTrue(lDs.size() > 0);
            DatasetInfo d = lDs.get(0);
            DatasetConfig conf = bs.getConfiguration(d);
            assertNotNull(conf);
            assertTrue(conf.getAttributePages().size() > 0);
            assertTrue(conf.getAttributePages().get(0).getAttributeGroups().size() > 0);
            assertTrue(conf.getAttributePages().get(0).getAttributeGroups().get(0).getAttributeCollections().size() > 0);

        } catch (BiomartServiceException ex) {
            log.error(ex.getMessage());
        }

    }
}
