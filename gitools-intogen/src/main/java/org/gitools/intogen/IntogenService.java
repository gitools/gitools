/*
 * #%L
 * gitools-intogen
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
package org.gitools.intogen;

import org.gitools.utils.fileutils.IOUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class IntogenService {

    private static IntogenService service;

    private IntogenService() {
    }

    public static IntogenService getDefault() {
        if (service == null) {
            service = new IntogenService();
        }

        return service;
    }

    public void queryFromPOST(File folder, String prefix, @NotNull URL action, @NotNull List<String[]> properties, @NotNull IProgressMonitor monitor) throws IntogenServiceException {

        try {
            monitor.begin("Querying for data ...", 1);

            // URL connection channel.
            URLConnection urlConn = action.openConnection();

            urlConn.setDoInput(true); // Let the run-time system (RTS) know that we want input.
            urlConn.setDoOutput(true); // Let the RTS know that we want to do output.
            urlConn.setUseCaches(false); // No caching, we want the real thing.
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // Specify the content type.

            // Send POST output.
            DataOutputStream printout = new DataOutputStream(urlConn.getOutputStream());

            StringBuilder content = new StringBuilder();
            boolean first = true;
            for (String[] entry : properties) {
                if (!first) {
                    content.append('&');
                }
                first = false;

                content.append(entry[0]).append('=');
                content.append(URLEncoder.encode(entry[1], "UTF-8"));
            }

            printout.writeBytes(content.toString());
            printout.flush();
            printout.close();

            //System.out.println(content.toString());

            monitor.end();

            Map<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("modulemap.tsv", prefix + "-oncomodules.tcm.gz");
            nameMap.put("oncomodules.tsv", prefix + "-annotations.tsv.gz");

            nameMap.put("modulemap.csv", prefix + "-oncomodules.tcm.gz");
            nameMap.put("oncomodules.csv", prefix + "-annotations.tsv.gz");

            nameMap.put("data.tsv", prefix + ".cdm.gz");
            nameMap.put("row.annotations.tsv", prefix + "-rows.tsv.gz");
            nameMap.put("column.annotations.tsv", prefix + "-columns.tsv.gz");

            monitor.begin("Downloading data ...", 1);

            // Get response data.
            ZipInputStream zin = new ZipInputStream(urlConn.getInputStream());

            ZipEntry ze;
            while ((ze = zin.getNextEntry()) != null) {
                IProgressMonitor mon = monitor.subtask();

                long totalKb = ze.getSize() / 1024;

                String name = nameMap.get(ze.getName());
                if (name == null) {
                    name = prefix + "." + ze.getName();
                }

                mon.begin("Extracting " + name + " ...", (int) ze.getSize());

                File outFile = new File(folder, name);
                if (!outFile.getParentFile().exists()) {
                    outFile.getParentFile().mkdirs();
                }

                OutputStream fout = IOUtils.openOutputStream(outFile);

                final int BUFFER_SIZE = 4 * 1024;
                byte[] data = new byte[BUFFER_SIZE];
                int partial = 0;
                int count;
                while ((count = zin.read(data, 0, BUFFER_SIZE)) != -1) {
                    fout.write(data, 0, count);
                    partial += count;
                    mon.info((partial / 1024) + " Kb read");
                    mon.worked(count);
                }
                zin.closeEntry();
                fout.close();

                mon.end();
            }

            zin.close();

            monitor.end();
        } catch (IOException ex) {
            throw new IntogenServiceException(ex);
        }
    }
}
