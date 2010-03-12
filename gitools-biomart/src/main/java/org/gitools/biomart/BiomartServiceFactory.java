/*
 *  Copyright 2010 xavier.
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
package org.gitools.biomart;

import org.gitools.biomart.settings.BiomartSource;
import org.gitools.biomart.settings.BiomartSourceManager;

/**
 *Factory of BiomartService
 * @author xrp02032010
 */
public class BiomartServiceFactory {

    private BiomartServiceFactory instance;

    public BiomartServiceFactory getInstance() {
        if (instance == null)
            instance = new BiomartServiceFactory();
        
        return instance;
    }

    private BiomartServiceFactory() {
    }

    /**
     * Creates a Biomart service from a Biomart source
     * @param source
     * @return biomart service
     */
    public static IBiomartService createService(BiomartSource source) throws BiomartServiceException {
        BiomartConfiguration bc = new BiomartConfiguration(source);
        IBiomartService bs = new BiomartGenericService(bc);
        return bs;
    }

    /**
     * Creates a Biomart service from a Biomart configuration
     * @param configuration
     * @return biomart service
     */
    public static IBiomartService createService(BiomartConfiguration config) throws BiomartServiceException {
        IBiomartService bs = new BiomartGenericService(config);
        return bs;
    }

    /**
     * Creates the default Biomart Service (currently through Biomart Central Portal)
     * @return biomart service
     */
    public static IBiomartService createDefaultservice() throws BiomartServiceException {

		BiomartSource bs = BiomartSourceManager.getDefault().getBiomartListSrc().getSources().get(0);

        return  createService(bs);
    }

    /*public static void main(String[] args) {

        BiomartSource b = Settings.getBiomartListSrc().getSource().get(0);
        try {
            IBiomartService r = BiomartServiceFactory.createService(b);
            List<Mart> l = r.getRegistry();
            System.out.println(l.get(0).getDatabase());
            //List<DatasetInfo> ld=r.getDatasets(l.get(0));
            //System.out.println(ld.get(0).getDisplayName());


        } catch (BiomartServiceException ex) {
            Logger.getLogger(BiomartServiceFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }*/
}
