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

package org.gitools.analysis.correlation;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.apache.commons.lang.ArrayUtils;
import org.gitools.analysis.AnalysisCommand;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.groupcomparison.ColumnGroup;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.groupcomparison.GroupComparisonProcessor;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.persistence.PersistenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;

public class GroupComparisonCommand extends AnalysisCommand {

    public static final String GROUP_BY_VALUE = "value";
    public static final String GROUP_BY_LABELS = "labels";
    protected GroupComparisonAnalysis analysis;
	protected String dataMime;
	protected String dataPath;
    private String groupingMethod;
    private String groups;

    public GroupComparisonCommand(
            GroupComparisonAnalysis analysis,
            String dataMime, String dataPath,
            String workdir, String fileName,
            String groupingMethod, String groups) {

		super(workdir, fileName);
		
		this.analysis = analysis;
		this.dataMime = dataMime;
		this.dataPath = dataPath;
        this.groupingMethod = groupingMethod;
        this.groups = groups;

    }



    private ColumnGroup[] getGrouping(String groupingMethod, String groups, BaseMatrix data) throws IOException {

        ColumnGroup[] columnGroups = new ColumnGroup[0];

        if (groupingMethod.equals(GroupComparisonCommand.GROUP_BY_LABELS)) {

            String[] groupDefs = groups.split(",");
            if (groupDefs.length == 2) {

                columnGroups = new ColumnGroup[2];
                int counter = 0;
                for (String filename : groupDefs) {
                    FileReader fileReader = new FileReader(filename);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    HashSet<Integer> colIndices = new HashSet<Integer>();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        int i = getColumnIndex(data,line);
                        if (i != -1)
                            colIndices.add(i);
                    }
                    bufferedReader.close();

                    int[] cols = ArrayUtils.toPrimitive(colIndices.toArray(new Integer[colIndices.size()]));

                    Integer groupNb = counter+1;
                    columnGroups[counter] = new ColumnGroup("Group" + groupNb.toString());
                    columnGroups[counter].setColumns(cols);
                    counter++;

                }
            }
        }
        return columnGroups;
    }

    private int getColumnIndex(BaseMatrix data, String line) {
        Object[] cols = data.getColumns().toArray();
        for (int i = 0; i < cols.length; i++) {
            String s = (String) cols[i];
            if (s.equals(line))
                return i;
        }
        return -1;
    }

    @Override
	public void run(IProgressMonitor monitor) throws AnalysisException {

		try {

            BaseMatrix data = loadDataMatrix(
                    new File(dataPath), dataMime, new Properties(), monitor);

            analysis.setData(data);


            try {
                ColumnGroup[] columnGroups = getGrouping(groupingMethod, groups, data);
                analysis.setGroup1(columnGroups[0]);
                analysis.setGroup2(columnGroups[1]);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

			GroupComparisonProcessor proc = new GroupComparisonProcessor(analysis);

			proc.run(monitor);

			File workdirFile = new File(workdir);
			if (!workdirFile.exists())
				workdirFile.mkdirs();

			File file = new File(workdirFile, fileName);
			PersistenceManager.getDefault().store(file, analysis, monitor);
		}
		catch (Throwable cause) {
			throw new AnalysisException(cause);
		}
	}

}
