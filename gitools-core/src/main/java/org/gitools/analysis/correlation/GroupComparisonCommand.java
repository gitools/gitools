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
package org.gitools.analysis.correlation;

import org.apache.commons.lang.ArrayUtils;
import org.gitools.analysis.AnalysisCommand;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.groupcomparison.ColumnGroup;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.groupcomparison.GroupComparisonProcessor;
import org.gitools.datafilters.BinaryCutoff;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.model.Attribute;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupComparisonCommand extends AnalysisCommand
{

    public static final String GROUP_BY_VALUE = "value";
    public static final String GROUP_BY_LABELS = "labels";
    protected GroupComparisonAnalysis analysis;
    protected String dataMime;
    protected String dataPath;
    private String groupingMethod;
    private String groups;
    private String[] groupDescriptions;

    public GroupComparisonCommand(
            GroupComparisonAnalysis analysis,
            String dataMime, String dataPath,
            String workdir, String fileName,
            String groupingMethod, String groups, String[] groupDescriptions)
    {

        super(workdir, fileName);

        this.analysis = analysis;
        this.dataMime = dataMime;
        this.dataPath = dataPath;
        this.groupingMethod = groupingMethod;
        this.groups = groups;
        this.groupDescriptions = groupDescriptions;

    }


    private ColumnGroup[] getGrouping(String groupingMethod, String groups, BaseMatrix data) throws IOException
    {

        ColumnGroup[] columnGroups = new ColumnGroup[0];
        String[] groupDefs = groups.split(",");
        columnGroups = new ColumnGroup[groupDefs.length];

        if (groupingMethod.equals(GroupComparisonCommand.GROUP_BY_LABELS))
        {

            if (groupDefs.length == 2)
            {

                int counter = 0;
                for (String filename : groupDefs)
                {
                    FileReader fileReader = new FileReader(filename);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    HashSet<Integer> colIndices = new HashSet<Integer>();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        int i = getColumnIndex(data, line);
                        if (i != -1)
                        {
                            colIndices.add(i);
                        }
                    }
                    bufferedReader.close();

                    int[] cols = ArrayUtils.toPrimitive(colIndices.toArray(new Integer[colIndices.size()]));

                    Integer groupNb = counter + 1;
                    columnGroups[counter] = new ColumnGroup("Group" + groupNb.toString());
                    columnGroups[counter].setColumns(cols);
                    counter++;

                }
            }
        }
        else if (groupingMethod.equals(GroupComparisonCommand.GROUP_BY_VALUE))
        {


            Pattern dataDimPatternAbs = Pattern.compile("\\|'(.+)'\\|", Pattern.CASE_INSENSITIVE);
            Pattern dataDimPattern = Pattern.compile("'(.+)'", Pattern.CASE_INSENSITIVE);
            boolean abs = false;

            for (int i = 0; i < groupDefs.length; i++)
            {
                String groupString = groupDefs[i];

                String dataDim = "";
                int dataDimIndex = -1;
                BinaryCutoff bc = null;

                Matcher matcherAbs = dataDimPatternAbs.matcher(groupString);
                Matcher matcher = dataDimPattern.matcher(groupString);

                if (matcherAbs.find())
                {
                    dataDim = matcherAbs.group(1);
                    abs = true;
                }
                else if (matcher.find())
                {
                    dataDim = matcher.group(1);
                }
                dataDimIndex = data.getCellAttributeIndex(dataDim);

                String[] parts = groupDefs[i].split(" ");
                int partsNb = parts.length;

                String cmpShortName = abs ? "abs " + parts[partsNb - 2] : parts[partsNb - 2];
                CutoffCmp cmp = CutoffCmp.getFromName(cmpShortName);
                Double value = Double.parseDouble(parts[partsNb - 1]);
                bc = new BinaryCutoff(cmp, value);

                String groupName;
                if (groupingMethod.equals(GROUP_BY_VALUE))
                {
                    groupName = dataDim + " " + cmp.getLongName() + " " + String.valueOf(value);
                }
                else
                {
                    groupName = "user defined Group";
                }

                columnGroups[i] = new ColumnGroup(groupName, null, bc, dataDimIndex);
            }
        }
        return columnGroups;
    }

    private List<Attribute> getGroupAttributes(ColumnGroup[] groups)
    {
        List<Attribute> analysisAttributes = new ArrayList<Attribute>();
        if (groupDescriptions.length > 1)
        {
            for (int i = 0; i < groupDescriptions.length; i++)
            {
                analysisAttributes.add(new Attribute("Group " + Integer.toString(i + 1), groupDescriptions[i]));
            }
        }
        else
        {
            for (int i = 0; i < groups.length; i++)
            {
                analysisAttributes.add(new Attribute("Group " + Integer.toString(i + 1), groups[i].getName()));
            }
        }
        return analysisAttributes;
    }

    private int getColumnIndex(BaseMatrix data, String line)
    {
        Object[] cols = data.getColumns().toArray();
        for (int i = 0; i < cols.length; i++)
        {
            String s = (String) cols[i];
            if (s.equals(line))
            {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void run(IProgressMonitor progressMonitor) throws AnalysisException
    {

        try
        {

            BaseMatrix data = loadDataMatrix(
                    new UrlResourceLocator(new File(dataPath)),
                    progressMonitor
            );

            analysis.setData(data);


            try
            {
                ColumnGroup[] columnGroups = getGrouping(groupingMethod, groups, data);
                List<Attribute> attributes = getGroupAttributes(columnGroups);
                analysis.setGroup1(columnGroups[0]);
                analysis.setGroup2(columnGroups[1]);
                analysis.setAttributes(attributes);

            } catch (IOException e)
            {
                e.printStackTrace();
            }

            GroupComparisonProcessor proc = new GroupComparisonProcessor(analysis);

            proc.run(progressMonitor);

            File workdirFile = new File(workdir);
            if (!workdirFile.exists())
            {
                workdirFile.mkdirs();
            }

            IResourceLocator resourceLocator = new UrlResourceLocator(new File(workdirFile, fileName));
            PersistenceManager.get().store(resourceLocator, analysis, progressMonitor);
        } catch (Throwable cause)
        {
            throw new AnalysisException(cause);
        }
    }

}
