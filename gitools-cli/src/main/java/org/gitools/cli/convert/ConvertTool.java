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

package org.gitools.cli.convert;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.StreamProgressMonitor;
import edu.upf.bg.tools.ToolDescriptor;
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import edu.upf.bg.tools.impl.AbstractTool;
import org.gitools.cli.GitoolsArguments;
import org.gitools.cli.Main;
import org.gitools.persistence.*;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

        if (args.version)
            Main.printVersion();

		if (args.inputFileName == null)
			throw new ToolValidationException("An input file is required.");

		if (args.inputFileFormat == null) {
			args.inputFileFormat = PersistenceManager.get()
					.getMimeFromFile(args.inputFileName);

			if (args.inputFileFormat == null)
				throw new ToolValidationException("Unknown input file format.\n" +
						"You can use the option -input-format");
		}

		if (args.outputFileName == null)
			throw new ToolValidationException("An output file is required.");

		if (args.outputFileFormat == null) {
			args.outputFileFormat = PersistenceManager.get()
					.getMimeFromFile(args.outputFileName);

			if (args.outputFileFormat == null)
				throw new ToolValidationException("Unknown output file format.\n" +
						"You can use the option -output-format");
		}
	}

	@Override
	public void run(Object argsObject) throws ToolException {
		super.run(argsObject);

		Arguments args = (Arguments) argsObject;

		List<Conversion> vc = new ArrayList<Conversion>();

		initConversionList(vc);

		String inputMime = mimeFromFormat(args.inputFileFormat, args.inputFileName);
		String outputMime = mimeFromFormat(args.outputFileFormat, args.outputFileName);

		Conversion targetConv = new Conversion(inputMime, outputMime);
		int convIndex = vc.indexOf(targetConv);
		if (convIndex < 0)
			throw new ToolException("Unsupportted conversion from '"
					+ args.inputFileFormat + "' to '" + args.outputFileFormat + "'");

		targetConv = vc.get(convIndex);
		if (targetConv.delegate == null)
			throw new ToolException("Unimplemented conversion from '"
					+ args.inputFileFormat + "' to '" + args.outputFileFormat + "'");

		IProgressMonitor monitor = new StreamProgressMonitor(System.out, args.verbose, args.debug);

		monitor.begin("Loading input file ...", 1);



		IResource resource = null;
		try {
            IResourceLocator resourceLocator = new UrlResourceLocator(new File(args.inputFileName));
            IResourceFormat format = PersistenceManager.get().getFormatByMime(inputMime);
			resource = PersistenceManager.get().load(resourceLocator, IResource.class, format, new Properties(), monitor.subtask());
		} catch (PersistenceException ex) {
			monitor.exception(ex);
			throw new ToolException(ex);
		}

		if (resource == null)
			throw new ToolException("Unexpected error loading " + args.inputFileName);

		monitor.end();

		IResource dstObject = null;
		try {
            dstObject = (IResource) targetConv.delegate.convert(
					args.inputFileFormat, resource, args.outputFileFormat, monitor);
		}
		catch (Exception ex) {
			monitor.exception(ex);
			throw new ToolException(ex);
		}

		monitor.begin("Saving output file ...", 1);

		try {
            IResourceLocator resourceLocator = new UrlResourceLocator(new File(args.outputFileName));
            IResourceFormat format = PersistenceManager.get().getFormatByMime(outputMime);
            PersistenceManager.get().store(resourceLocator, dstObject, format, monitor.subtask());
		} catch (PersistenceException ex) {
			monitor.exception(ex);
			throw new ToolException(ex);
		}

		monitor.end();
	}

	private void initConversionList(List<Conversion> vc) {
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

	protected static final String LIST_L_FMT = "\t* %-48s%s";

	protected String mimeFromFormat(String format, String fileName) {
		String mime = null;
		if (fileName == null)
			return null;

		if (format != null) {
			// Try with file extension first
			mime = PersistenceManager.get().getMimeFromFile("fake." + format);
			if (mime == null)
				mime = format; // it should be mime type then
			//TODO check valid mime
		}
		else
			mime = PersistenceManager.get().getMimeFromFile(fileName);

		return mime;
	}

	@Override
	public void printUsage(PrintStream outputStream, String appName, ToolDescriptor toolDesc, CmdLineParser parser) {
		super.printUsage(outputStream, appName, toolDesc, parser);

		outputStream.println();

		outputStream.println("Supported formats:");
		FileFormat[] formats = new FileFormat[] {
			FileFormats.DOUBLE_MATRIX,
			FileFormats.DOUBLE_BINARY_MATRIX,
			FileFormats.GENE_MATRIX,
			FileFormats.GENE_MATRIX_TRANSPOSED,
			FileFormats.MODULES_2C_MAP
		};

		for (FileFormat f : formats)
			outputStream.println(String.format(LIST_L_FMT, f.getExtension() + " (" + f.getMime() + ")", f.getTitle()));
	}
}
