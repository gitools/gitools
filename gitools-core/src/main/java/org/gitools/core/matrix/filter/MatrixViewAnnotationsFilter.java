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
package org.gitools.core.matrix.filter;

import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.label.AnnotationsPatternProvider;
import org.gitools.core.label.LabelProvider;
import org.gitools.core.label.MatrixDimensionLabelProvider;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class MatrixViewAnnotationsFilter {

    public enum FilterDimension {
        ROWS, COLUMNS
    }

    private static interface LabelFilter {
        int matchIndex(String label);
    }

    private static class StringFilter implements LabelFilter {
        private final Map<String, Integer> values;

        public StringFilter(@NotNull List<String> values) {
            this.values = new HashMap<String, Integer>();
            for (int i = 0; i < values.size(); i++) {
                String v = values.get(i).trim();
                if (!v.isEmpty()) {
                    this.values.put(v, i);
                }
            }
        }

        @Override
        public int matchIndex(String label) {
            Integer index = values.get(label);
            return index != null ? index : -1;
        }
    }

    private static class RegexFilter implements LabelFilter {
        private final List<Pattern> patterns;

        public RegexFilter(@NotNull List<String> values) {
            patterns = new ArrayList<Pattern>(values.size());
            for (String value : values)
                if (!value.trim().isEmpty()) {
                    patterns.add(Pattern.compile(value));
                }
        }

        @Override
        public int matchIndex(String label) {
            for (int i = 0; i < patterns.size(); i++) {
                Pattern pat = patterns.get(i);
                if (pat.matcher(label).matches()) {
                    return i;
                }
            }
            return -1;
        }
    }

    public static void filter(HeatmapDimension heatmapDimension, @NotNull String pattern, @NotNull List<String> values, boolean useRegex) {

        LabelProvider labelProvider = new MatrixDimensionLabelProvider(heatmapDimension);
        if (!pattern.equalsIgnoreCase("${id}")) {
            labelProvider = new AnnotationsPatternProvider(heatmapDimension, pattern);
        }

        heatmapDimension.setVisible(filterLabels(labelProvider, values, useRegex, heatmapDimension.getVisible()));
    }

    @NotNull
    public static int[] filterLabels(@NotNull LabelProvider labelProvider, @NotNull List<String> values, boolean useRegex, int[] visibleIndices) {

        LabelFilter filter = null;
        if (useRegex) {
            filter = new RegexFilter(values);
        } else {
            filter = new StringFilter(values);
        }

        final List<Integer> selectedIndices = new ArrayList<Integer>();
        final List<Integer> matchIndices = new ArrayList<Integer>();

        int count = labelProvider.getCount();
        for (int index = 0; index < count; index++) {
            String label = labelProvider.getLabel(index);
            int mi = filter.matchIndex(label);
            if (mi != -1) {
                selectedIndices.add(visibleIndices[index]);
                matchIndices.add(mi);
            }
        }

        Integer[] sortIndices = new Integer[selectedIndices.size()];
        for (int i = 0; i < sortIndices.length; i++)
            sortIndices[i] = i;

        Arrays.sort(sortIndices, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                int d1 = matchIndices.get(i1);
                int d2 = matchIndices.get(i2);
                return d1 - d2;
            }
        });

        int[] vIndices = new int[sortIndices.length];
        for (int i = 0; i < vIndices.length; i++)
            vIndices[i] = selectedIndices.get(sortIndices[i]);

        return vIndices;
    }
}
