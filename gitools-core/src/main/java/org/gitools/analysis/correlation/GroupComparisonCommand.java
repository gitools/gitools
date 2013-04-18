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
import org.gitools.analysis.combination.ConvertModuleMapToMatrixResourceReference;
import org.gitools.analysis.groupcomparison.ColumnGroup;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.groupcomparison.GroupComparisonProcessor;
import org.gitools.datafilters.BinaryCutoff;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.Property;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupComparisonCommand extends AnalysisCommand {

    public static final String GROUP_BY_VALUE = "value";
    public static final String GROUP_BY_LABELS = "labels";
    private final GroupComparisonAnalysis analysis;
    private final IResourceFormat dataFormat;
    private final String dataPath;
    private final String groupingMethod;
    private final String groups;
    private final String[] groupDescriptions;

    public GroupComparisonCommand(GroupComparisonAnalysis analysis, IResourceFormat dataFormat, String dataPath, String workdir, String fileName, String groupingMethod, String groups, String[] groupDescriptions) {

        super(workdir, fileName);

        this.analysis = analysis;
        this.dataFormat = dataFormat;
        this.dataPath = dataPath;
        this.groupingMethod = groupingMethod;
        this.groups = groups;
        this.groupDescriptions = groupDescriptions;

    }


    @NotNull
    private ColumnGroup[] getGrouping(@NotNull String groupingMethod, @NotNull String groups, @NotNull IMatrix data) throws IOException {

        ColumnGroup[] columnGroups = new ColumnGroup[0];
        String[] groupDefs = groups.split(",");
        columnGroups = new ColumnGroup[groupDefs.length];

        if (groupingMethod.equals(GroupComparisonCommand.GROUP_BY_LABELS)) {

            if (groupDefs.length == 2) {

                int counter = 0;
                for (String filename : groupDefs) {
                    FileReader fileReader = new FileReader(filename);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    HashSet<Integer> colIndices = new HashSet<Integer>();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        int i = data.getColumns().getIndex(line);
                        if (i != -1) {
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
        } else if (groupingMethod.equals(GroupComparisonCommand.GROUP_BY_VALUE)) {


            Pattern dataDimPatternAbs = Pattern.compile("\\|'(.+)'\\|", Pattern.CASE_INSENSITIVE);
            Pattern dataDimPattern = Pattern.compile("'(.+)'", Pattern.CASE_INSENSITIVE);
            boolean abs = false;

            for (int i = 0; i < groupDefs.length; i++) {
                String groupString = groupDefs[i];

                String dataDim = "";
                int dataDimIndex = -1;
                BinaryCutoff bc = null;

                Matcher matcherAbs = dataDimPatternAbs.matcher(groupString);
                Matcher matcher = dataDimPattern.matcher(groupString);

                if (matcherAbs.find()) {
                    dataDim = matcherAbs.group(1);
                    abs = true;
                } else if (matcher.find()) {
                    dataDim = matcher.group(1);
                }
                dataDimIndex = data.getLayers().findId(dataDim);

                String[] parts = groupDefs[i].split(" ");
                int partsNb = parts.length;

                String cmpShortName = abs ? "abs " + parts[partsNb - 2] : parts[partsNb - 2];
                CutoffCmp cmp = CutoffCmp.getFromName(cmpShortName);
                Double value = Double.parseDouble(parts[partsNb - 1]);
                bc = new BinaryCutoff(cmp, value);

                String groupName;
                if (groupingMethod.equals(GROUP_BY_VALUE)) {
                    groupName = dataDim + " " + cmp.getLongName() + " " + String.valueOf(value);
                } else {
                    groupName = "user defined Group";
                }

                columnGroups[i] = new ColumnGroup(groupName, null, bc, dataDimIndex);
            }
        }
        return columnGroups;
    }

    @NotNull
    private List<Property> getGroupAttributes(@NotNull ColumnGroup[] groups) {
        List<Property> analysisAttributes = new ArrayList<Property>();
        if (groupDescriptions.length > 1) {
            for (int i = 0; i < groupDescriptions.length; i++) {
                analysisAttributes.add(new Property("Group " + Integer.toString(i + 1), groupDescriptions[i]));
            }
        } else {
            for (int i = 0; i < groups.length; i++) {
                analysisAttributes.add(new Property("Group " + Integer.toString(i + 1), groups[i].getName()));
            }
        }
        return analysisAttributes;
    }

    @Override
    public void run(IProgressMonitor progressMonitor) throws AnalysisException {

        try {
            ResourceReference<IMatrix> data = new ConvertModuleMapToMatrixResourceReference(new UrlResourceLocator(new File(dataPath)), dataFormat);
            analysis.setData(data);
            data.load(progressMonitor);

            try {
                ColumnGroup[] columnGroups = getGrouping(groupingMethod, groups, data.get());
                List<Property> attributes = getGroupAttributes(columnGroups);
                analysis.setGroup1(columnGroups[0]);
                analysis.setGroup2(columnGroups[1]);
                analysis.setProperties(attributes);

            } catch (IOException e) {
                e.printStackTrace();
            }

            GroupComparisonProcessor proc = new GroupComparisonProcessor(analysis);

            proc.run(progressMonitor);

            File workdirFile = new File(workdir);
            if (!workdirFile.exists()) {
                workdirFile.mkdirs();
            }

            IResourceLocator resourceLocator = new UrlResourceLocator(new File(workdirFile, fileName));
            PersistenceManager.get().store(resourceLocator, analysis, progressMonitor);
        } catch (Throwable cause) {
            throw new AnalysisException(cause);
        }
    }

}
