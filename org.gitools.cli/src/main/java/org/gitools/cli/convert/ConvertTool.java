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
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.cli.GitoolsArguments;
import org.gitools.cli.Main;
import static org.gitools.api.ApplicationContext.getPersistenceManager;
import org.gitools.analysis._DEPRECATED.formats.FileFormat;
import org.gitools.analysis._DEPRECATED.formats.FileFormats;
import org.gitools.analysis._DEPRECATED.formats.matrix.CmatrixMatrixFormat;
import org.gitools.analysis._DEPRECATED.formats.matrix.TdmMatrixFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.utils.progressmonitor.StreamProgressMonitor;
import org.gitools.utils.tools.ToolDescriptor;
import org.gitools.utils.tools.exception.ToolException;
import org.gitools.utils.tools.exception.ToolValidationException;
import org.gitools.utils.tools.impl.AbstractTool;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ConvertTool extends AbstractTool {

    public static class Arguments extends GitoolsArguments {

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
    public void validate(Object argsObject) throws ToolException {
        super.validate(argsObject);

        Arguments args = (Arguments) argsObject;

        if (args.version) {
            Main.printVersion();
        }

        if (args.inputFileName == null) {
            throw new ToolValidationException("An input file is required.");
        }

        if (args.inputFileFormat == null) {
            args.inputFileFormat = getPersistenceManager().getFormatExtension(args.inputFileName);

            if (args.inputFileFormat == null) {
                throw new ToolValidationException("Unknown input file format.\n" + "You can use the option -input-format");
            }
        }

        if (args.outputFileName == null) {
            throw new ToolValidationException("An output file is required.");
        }

        if (args.outputFileFormat == null) {
            args.outputFileFormat = getPersistenceManager().getFormatExtension(args.outputFileName);

            if (args.outputFileFormat == null) {
                throw new ToolValidationException("Unknown output file format.\n" + "You can use the option -output-format");
            }
        }
    }

    @Override
    public void run(Object argsObject) throws ToolException {
        super.run(argsObject);

        Arguments args = (Arguments) argsObject;

        List<Conversion> vc = new ArrayList<>();

        initConversionList(vc);

        String inputMime = mimeFromFormat(args.inputFileFormat, args.inputFileName);
        String outputMime = mimeFromFormat(args.outputFileFormat, args.outputFileName);

        Conversion targetConv = new Conversion(inputMime, outputMime);
        int convIndex = vc.indexOf(targetConv);

        IProgressMonitor monitor = new StreamProgressMonitor(System.out, args.verbose, args.debug);

        if (convIndex < 0) {
            // We don't want to load all the tdm in memory this is why this conversion is not possible
            // using the standard interface.
            //TODO rethink the interface
            if (inputMime.equals(TdmMatrixFormat.EXSTENSION) && outputMime.equals(CmatrixMatrixFormat.EXTENSION)) {
                FileCompressMatrixConversion converter = new FileCompressMatrixConversion();
                converter.convert(args.inputFileName, args.outputFileName, monitor);
                return;
            }

            throw new ToolException("Unsupportted conversion from '" + args.inputFileFormat + "' to '" + args.outputFileFormat + "'");
        }

        targetConv = vc.get(convIndex);
        if (targetConv.delegate == null) {
            throw new ToolException("Unimplemented conversion from '" + args.inputFileFormat + "' to '" + args.outputFileFormat + "'");
        }


        monitor.begin("Loading input file ...", 1);


        IResource resource = null;
        try {
            IResourceLocator resourceLocator = new UrlResourceLocator(new File(args.inputFileName));
            IResourceFormat format = getPersistenceManager().getFormat(inputMime, IResource.class);
            resource = getPersistenceManager().load(resourceLocator, format, monitor.subtask());
        } catch (PersistenceException ex) {
            monitor.exception(ex);
            throw new ToolException(ex);
        }

        if (resource == null) {
            throw new ToolException("Unexpected error loading " + args.inputFileName);
        }

        monitor.end();

        IResource dstObject = null;
        try {
            dstObject = (IResource) targetConv.delegate.convert(args.inputFileFormat, resource, args.outputFileFormat, monitor);
        } catch (Exception ex) {
            monitor.exception(ex);
            throw new ToolException(ex);
        }

        monitor.begin("Saving output file ...", 1);

        try {
            IResourceLocator resourceLocator = new UrlResourceLocator(new File(args.outputFileName));
            IResourceFormat format = getPersistenceManager().getFormat(outputMime, IResource.class);
            getPersistenceManager().store(resourceLocator, dstObject, format, monitor.subtask());
        } catch (PersistenceException ex) {
            monitor.exception(ex);
            throw new ToolException(ex);
        }

        monitor.end();
    }

    private static final String DOUBLE_MATRIX = "application/gitools-matrix-double";
    private static final String DOUBLE_BINARY_MATRIX = "application/gitools-matrix-binary";
    private static final String GENE_MATRIX = "application/gitools-gmx";
    private static final String GENE_MATRIX_TRANSPOSED = "application/gitools-gmt";
    private static final String MODULES_INDEXED_MAP = "application/gitools-modules-indexed";
    private static final String MODULES_2C_MAP = "application/gitools-modules-2c";

    private void initConversionList(List<Conversion> vc) {
        //TODO vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.DOUBLE_MATRIX, new MatrixConversion()));
        //TODO vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.GENE_MATRIX, new MatrixConversion()));
        //TODO vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.GENE_MATRIX_TRANSPOSED, new MatrixConversion()));
        vc.add(new Conversion(DOUBLE_BINARY_MATRIX, MODULES_2C_MAP, new MatrixToModulesConversion()));
        vc.add(new Conversion(DOUBLE_BINARY_MATRIX, MODULES_INDEXED_MAP, new MatrixToModulesConversion()));

        //TODO vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.DOUBLE_BINARY_MATRIX, new MatrixConversion()));
        //TODO vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.GENE_MATRIX, new MatrixConversion()));
        //TODO vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.GENE_MATRIX_TRANSPOSED, new MatrixConversion()));
        vc.add(new Conversion(DOUBLE_MATRIX, MODULES_2C_MAP, new MatrixToModulesConversion()));
        vc.add(new Conversion(DOUBLE_MATRIX, MODULES_INDEXED_MAP, new MatrixToModulesConversion()));

        //TODO vc.add(new Conversion(MimeTypes.GENE_MATRIX, MimeTypes.DOUBLE_BINARY_MATRIX, new MatrixConversion()));
        //TODO vc.add(new Conversion(MimeTypes.GENE_MATRIX, MimeTypes.DOUBLE_MATRIX, new MatrixConversion()));
        vc.add(new Conversion(GENE_MATRIX, MODULES_2C_MAP, new MatrixToModulesConversion()));
        vc.add(new Conversion(GENE_MATRIX, MODULES_INDEXED_MAP, new MatrixToModulesConversion()));

        //TODO vc.add(new Conversion(MimeTypes.GENE_MATRIX_TRANSPOSED, MimeTypes.DOUBLE_BINARY_MATRIX, new MatrixConversion()));
        //TODO vc.add(new Conversion(MimeTypes.GENE_MATRIX_TRANSPOSED, MimeTypes.DOUBLE_MATRIX, new MatrixConversion()));
        vc.add(new Conversion(GENE_MATRIX_TRANSPOSED, MODULES_2C_MAP, new MatrixToModulesConversion()));
        vc.add(new Conversion(GENE_MATRIX_TRANSPOSED, MODULES_INDEXED_MAP, new MatrixToModulesConversion()));

        vc.add(new Conversion(MODULES_2C_MAP, MODULES_INDEXED_MAP, new ModulesConversion()));
        vc.add(new Conversion(MODULES_2C_MAP, DOUBLE_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MODULES_2C_MAP, DOUBLE_BINARY_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MODULES_2C_MAP, GENE_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MODULES_2C_MAP, GENE_MATRIX_TRANSPOSED, new ModulesToMatrixConversion()));

        vc.add(new Conversion(MODULES_INDEXED_MAP, MODULES_2C_MAP, new ModulesConversion()));
        vc.add(new Conversion(MODULES_INDEXED_MAP, DOUBLE_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MODULES_INDEXED_MAP, DOUBLE_BINARY_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MODULES_INDEXED_MAP, GENE_MATRIX, new ModulesToMatrixConversion()));
        vc.add(new Conversion(MODULES_INDEXED_MAP, GENE_MATRIX_TRANSPOSED, new ModulesToMatrixConversion()));
    }

    private static final String LIST_L_FMT = "\t* %-48s%s";

    String mimeFromFormat(String format, String fileName) {
        if (StringUtils.isEmpty(format)) {
            format = fileName;
        }

        return getPersistenceManager().getFormatExtension(format);
    }

    @Override
    public void printUsage(PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser) {
        super.printUsage(outputStream, appName, toolDesc, parser);

        outputStream.println();

        outputStream.println("Supported formats:");
        FileFormat[] formats = new FileFormat[]{FileFormats.DOUBLE_MATRIX, FileFormats.DOUBLE_BINARY_MATRIX, FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED, FileFormats.MODULES_2C_MAP};

        for (FileFormat f : formats)
            outputStream.println(String.format(LIST_L_FMT, f.getExtension(), f.getTitle()));
    }
}
