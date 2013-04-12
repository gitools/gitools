/*
 * #%L
 * gitools-cli
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
package org.gitools.cli;

import org.apache.commons.lang.StringUtils;
import org.gitools.model.Analysis;
import org.gitools.model.KeyValue;
import org.gitools.model.Property;
import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceFormat;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.utils.tools.exception.ToolException;
import org.gitools.utils.tools.exception.ToolValidationException;
import org.gitools.utils.tools.impl.AbstractTool;
import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @noinspection ALL
 */
public class AnalysisTool extends AbstractTool
{

    protected static final String LIST_S_FMT = "\t* %-16s%s";
    protected static final String LIST_L_FMT = "\t* %-48s%s";

    @NotNull
    private final List<Property> analysisAttributes = new ArrayList<Property>(0);

    @Override
    public void validate(Object argsObject) throws ToolException
    {
        super.validate(argsObject);

        AnalysisArguments args = (AnalysisArguments) argsObject;

        if (args.version)
        {
            Main.printVersion();
        }

        if (args.analysisTitle == null)
        {
            args.analysisTitle = args.analysisName;
        }

        for (String attr : args.analysisAttributes)
        {
            final String[] a = attr.split("=", 2);
            if (a.length != 2)
            {
                throw new ToolValidationException("Malformed analysis attribute: " + attr);
            }
            analysisAttributes.add(new Property(a[0], a[1]));
        }
    }

    protected IResourceFormat getResourceFormat(String extension, String fileName, Class<? extends IResource> resourceClass)
    {

        if (StringUtils.isEmpty(extension))
        {
            // Use the fileName if the user has not specify any format
            extension = fileName;
        }

        return PersistenceManager.get().getFormat(extension, resourceClass);
    }

    @NotNull
    protected List<KeyValue> parseConfiguration(@NotNull List<String> config) throws ToolValidationException
    {
        List<KeyValue> kv = new ArrayList<KeyValue>(config.size());
        for (String conf : config)
        {
            final String[] c = conf.split("=", 2);
            if (c.length != 2)
            {
                throw new ToolValidationException("Malformed configuration parameter: " + conf);
            }
            kv.add(new KeyValue(c[0], c[1]));
        }
        return kv;
    }

    @NotNull
    protected Properties parseProperties(@NotNull List<String> config) throws ToolValidationException
    {
        Properties properties = new Properties();
        for (String conf : config)
        {
            final String[] c = conf.split("=", 2);
            if (c.length != 2)
            {
                throw new ToolValidationException("Malformed configuration parameter: " + conf);
            }
            properties.setProperty(c[0], c[1]);
        }
        return properties;
    }

    protected void prepareGeneralAnalysisAttributes(@NotNull Analysis analysis, @NotNull AnalysisArguments args)
    {
        analysis.setTitle(args.analysisTitle);
        analysis.setDescription(args.analysisNotes);
        analysis.setProperties(analysisAttributes);
    }

    protected void printDataFormats(@NotNull PrintStream out)
    {
        out.println("Supported data formats:");
        FileFormat[] formats = new FileFormat[]{FileFormats.DOUBLE_MATRIX, FileFormats.DOUBLE_BINARY_MATRIX, FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED, FileFormats.MODULES_2C_MAP};

        for (FileFormat f : formats)
            out.println(String.format(LIST_L_FMT, f.getExtension(), f.getTitle()));
    }

    protected void printModulesFormats(@NotNull PrintStream out)
    {
        out.println("Supported modules formats:");
        FileFormat[] formats = new FileFormat[]{FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED, FileFormats.MODULES_2C_MAP, FileFormats.DOUBLE_BINARY_MATRIX};

        for (FileFormat f : formats)
            out.println(String.format(LIST_L_FMT, f.getExtension(), f.getTitle()));
    }
}
