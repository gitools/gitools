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
package org.gitools.cli.convert;

import org.apache.commons.lang.StringUtils;
import org.gitools.cli.GitoolsArguments;
import org.gitools.cli.Main;
import org.gitools.persistence.*;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.persistence.formats.compressmatrix.CompressMatrixFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.gitools.utils.tools.ToolDescriptor;
import org.gitools.utils.tools.exception.ToolException;
import org.gitools.utils.tools.exception.ToolValidationException;
import org.gitools.utils.tools.impl.AbstractTool;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ConvertTool extends AbstractTool
{

    public static class Arguments extends GitoolsArguments
    {

        @Option(name = "-i", aliases = "-input", metaVar = "<path>",
                usage = "Input file.")
        public String inputFileName;

        @Option(name = "-if", aliases = "-input-format", metaVar = "<FORMAT>",
                usage = "Input file format.")
        public String inputFileFormat;

        @Option(name = "-o", aliases = "-output", metaVar = "<path>",
                usage = "Output file.")
        public String outputFileName;

        @Option(name = "-of", aliases = "-output-format", metaVar = "<FORMAT>",
                usage = "Output file format.")
        public String outputFileFormat;
    }

    @Override
    public void validate(Object argsObject) throws ToolException
    {
        super.validate(argsObject);

        Arguments args = (Arguments) argsObject;

        if (args.version)
        {
            Main.printVersion();
        }

        if (args.inputFileName == null)
        {
            throw new ToolValidationException("An input file is required.");
        }

        if (args.inputFileFormat == null)
        {
            args.inputFileFormat = PersistenceManager.get().getFormatExtension(args.inputFileName);

            if (args.inputFileFormat == null)
            {
                throw new ToolValidationException("Unknown input file format.\n" + "You can use the option -input-format");
            }
        }

        if (args.outputFileName == null)
        {
            throw new ToolValidationException("An output file is required.");
        }

        if (args.outputFileFormat == null)
        {
            args.outputFileFormat = PersistenceManager.get().getFormatExtension(args.outputFileName);

            if (args.outputFileFormat == null)
            {
                throw new ToolValidationException("Unknown output file format.\n" + "You can use the option -output-format");
            }
        }
    }

    @Override
    public void run(Object argsObject) throws ToolException
    {
        super.run(argsObject);

        Arguments args = (Arguments) argsObject;

        List<Conversion> vc = new ArrayList<Conversion>();

        initConversionList(vc);

        String inputMime = mimeFromFormat(args.inputFileFormat, args.inputFileName);
        String outputMime = mimeFromFormat(args.outputFileFormat, args.outputFileName);

        Conversion targetConv = new Conversion(inputMime, outputMime);
        int convIndex = vc.indexOf(targetConv);

        IProgressMonitor monitor = new StreamProgressMonitor(System.out, args.verbose, args.debug);

        if (convIndex < 0)
        {
            // We don't want to load all the tdm in memory this is why this conversion is not possible
            // using the standard interface.
            //TODO rethink the interface
            if (inputMime.equals(FileSuffixes.OBJECT_MATRIX) && outputMime.equals(CompressMatrixFormat.EXTENSION))
            {
                FileCompressMatrixConversion converter = new FileCompressMatrixConversion();
                converter.convert(args.inputFileName, args.outputFileName, monitor);
                return;
            }

            throw new ToolException("Unsupportted conversion from '" + args.inputFileFormat + "' to '" + args.outputFileFormat + "'");
        }

        targetConv = vc.get(convIndex);
        if (targetConv.delegate == null)
        {
            throw new ToolException("Unimplemented conversion from '" + args.inputFileFormat + "' to '" + args.outputFileFormat + "'");
        }



        monitor.begin("Loading input file ...", 1);


        IResource resource = null;
        try
        {
            IResourceLocator resourceLocator = new UrlResourceLocator(new File(args.inputFileName));
            IResourceFormat format = PersistenceManager.get().getFormat(inputMime, IResource.class);
            resource = PersistenceManager.get().load(resourceLocator, format, monitor.subtask());
        } catch (PersistenceException ex)
        {
            monitor.exception(ex);
            throw new ToolException(ex);
        }

        if (resource == null)
        {
            throw new ToolException("Unexpected error loading " + args.inputFileName);
        }

        monitor.end();

        IResource dstObject = null;
        try
        {
            dstObject = (IResource) targetConv.delegate.convert(args.inputFileFormat, resource, args.outputFileFormat, monitor);
        } catch (Exception ex)
        {
            monitor.exception(ex);
            throw new ToolException(ex);
        }

        monitor.begin("Saving output file ...", 1);

        try
        {
            IResourceLocator resourceLocator = new UrlResourceLocator(new File(args.outputFileName));
            IResourceFormat format = PersistenceManager.get().getFormat(outputMime, IResource.class);
            PersistenceManager.get().store(resourceLocator, dstObject, format, monitor.subtask());
        } catch (PersistenceException ex)
        {
            monitor.exception(ex);
            throw new ToolException(ex);
        }

        monitor.end();
    }

    private void initConversionList(@NotNull List<Conversion> vc)
    {
        vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.DOUBLE_MATRIX, new MatrixConversion()));
        vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.GENE_MATRIX, new MatrixConversion()));
        vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.GENE_MATRIX_TRANSPOSED, new MatrixConversion()));
        vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.MODULES_2C_MAP, new MatrixToModulesConversion()));
        vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.MODULES_INDEXED_MAP, new MatrixToModulesConversion()));

        vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.DOUBLE_BINARY_MATRIX, new MatrixConversion()));
        vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.GENE_MATRIX, new MatrixConversion()));
        vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.GENE_MATRIX_TRANSPOSED, new MatrixConversion()));
        vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.MODULES_2C_MAP, new MatrixToModulesConversion()));
        vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.MODULES_INDEXED_MAP, new MatrixToModulesConversion()));

        vc.add(new Conversion(MimeTypes.GENE_MATRIX, MimeTypes.DOUBLE_BINARY_MATRIX, new MatrixConversion()));
        vc.add(new Conversion(MimeTypes.GENE_MATRIX, MimeTypes.DOUBLE_MATRIX, new MatrixConversion()));
        vc.add(new Conversion(MimeTypes.GENE_MATRIX, MimeTypes.MODULES_2C_MAP, new MatrixToModulesConversion()));
        vc.add(new Conversion(MimeTypes.GENE_MATRIX, MimeTypes.MODULES_INDEXED_MAP, new MatrixToModulesConversion()));

        vc.add(new Conversion(MimeTypes.GENE_MATRIX_TRANSPOSED, MimeTypes.DOUBLE_BINARY_MATRIX, new MatrixConversion()));
        vc.add(new Conversion(MimeTypes.GENE_MATRIX_TRANSPOSED, MimeTypes.DOUBLE_MATRIX, new MatrixConversion()));
        vc.add(new Conversion(MimeTypes.GENE_MATRIX_TRANSPOSED, MimeTypes.MODULES_2C_MAP, new MatrixToModulesConversion()));
        vc.add(new Conversion(MimeTypes.GENE_MATRIX_TRANSPOSED, MimeTypes.MODULES_INDEXED_MAP, new MatrixToModulesConversion()));

        vc.add(new Conversion(MimeTypes.MODULES_2C_MAP, MimeTypes.MODULES_INDEXED_MAP, new ModulesConversion()));
        vc.add(new Conversion(MimeTypes.MODULES_2C_MAP, MimeTypes.DOUBLE_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MimeTypes.MODULES_2C_MAP, MimeTypes.DOUBLE_BINARY_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MimeTypes.MODULES_2C_MAP, MimeTypes.GENE_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MimeTypes.MODULES_2C_MAP, MimeTypes.GENE_MATRIX_TRANSPOSED, new ModulesToMatrixConversion()));

        vc.add(new Conversion(MimeTypes.MODULES_INDEXED_MAP, MimeTypes.MODULES_2C_MAP, new ModulesConversion()));
        vc.add(new Conversion(MimeTypes.MODULES_INDEXED_MAP, MimeTypes.DOUBLE_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MimeTypes.MODULES_INDEXED_MAP, MimeTypes.DOUBLE_BINARY_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MimeTypes.MODULES_INDEXED_MAP, MimeTypes.GENE_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MimeTypes.MODULES_INDEXED_MAP, MimeTypes.GENE_MATRIX_TRANSPOSED, new ModulesToMatrixConversion()));
    }

    private static final String LIST_L_FMT = "\t* %-48s%s";

    String mimeFromFormat(String format, String fileName)
    {
        if (StringUtils.isEmpty(format))
        {
            format = fileName;
        }

        return PersistenceManager.get().getFormatExtension(format);
    }

    @Override
    public void printUsage(@NotNull PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser)
    {
        super.printUsage(outputStream, appName, toolDesc, parser);

        outputStream.println();

        outputStream.println("Supported formats:");
        FileFormat[] formats = new FileFormat[]{FileFormats.DOUBLE_MATRIX, FileFormats.DOUBLE_BINARY_MATRIX, FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED, FileFormats.MODULES_2C_MAP};

        for (FileFormat f : formats)
            outputStream.println(String.format(LIST_L_FMT, f.getExtension(), f.getTitle()));
    }
}
