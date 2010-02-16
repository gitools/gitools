/*
 *  Copyright 2010 cperez.
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
import edu.upf.bg.tools.exception.ToolException;
import edu.upf.bg.tools.exception.ToolValidationException;
import edu.upf.bg.tools.impl.AbstractTool;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.gitools.cli.GitoolsArguments;
import org.gitools.persistence.MimeTypeManager;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.kohsuke.args4j.Option;

public class ConvertTool extends AbstractTool {

	public static class Arguments extends GitoolsArguments {

		@Option(name = "-i", aliases = "-input", metaVar = "<path>",
				usage = "Input file.")
		public String inputFileName;

		@Option(name = "-im", aliases = "-input-mime", metaVar = "<MIME>",
				usage = "Input file MIME type.")
		public String inputFileMime;

		@Option(name = "-i", aliases = "-output", metaVar = "<path>",
				usage = "Output file.")
		public String outputFileName;

		@Option(name = "-im", aliases = "-output-mime", metaVar = "<MIME>",
				usage = "Output file MIME type.")
		public String outputFileMime;
	}

	@Override
	public void validate(Object argsObject) throws ToolException {
		super.validate(argsObject);

		Arguments args = (Arguments) argsObject;

		if (args.inputFileName == null)
			throw new ToolValidationException("An input file is required.");

		if (args.inputFileMime == null) {
			args.inputFileMime = MimeTypeManager.getDefault()
					.fromFile(new File(args.inputFileName));

			if (args.inputFileMime == null)
				throw new ToolValidationException("Unknown input file type, a mime type is required.\n" +
						"You can use the option -input-mime");
		}

		if (args.outputFileName == null)
			throw new ToolValidationException("An output file is required.");

		if (args.outputFileMime == null) {
			args.outputFileMime = MimeTypeManager.getDefault()
					.fromFile(new File(args.outputFileName));

			if (args.outputFileMime == null)
				throw new ToolValidationException("Unknown output file type, a mime type is required.\n" +
						"You can use the option -output-mime");
		}
	}

	@Override
	public void run(Object argsObject) throws ToolException {
		super.run(argsObject);

		Arguments args = (Arguments) argsObject;

		List<Conversion> vc = new ArrayList<Conversion>();

		vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.DOUBLE_MATRIX, new MatrixConversion()));
		vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.ELEMENT_LISTS, new MatrixConversion()));
		vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.MODULES_2C_MAP, null));
		vc.add(new Conversion(MimeTypes.DOUBLE_BINARY_MATRIX, MimeTypes.MODULES_INDEXED_MAP, null));

		vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.DOUBLE_BINARY_MATRIX, new MatrixConversion()));
		vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.ELEMENT_LISTS, new MatrixConversion()));
		vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.MODULES_2C_MAP, null));
		vc.add(new Conversion(MimeTypes.DOUBLE_MATRIX, MimeTypes.MODULES_INDEXED_MAP, null));

		vc.add(new Conversion(MimeTypes.ELEMENT_LISTS, MimeTypes.DOUBLE_BINARY_MATRIX, new MatrixConversion()));
		vc.add(new Conversion(MimeTypes.ELEMENT_LISTS, MimeTypes.DOUBLE_MATRIX, new MatrixConversion()));
		vc.add(new Conversion(MimeTypes.ELEMENT_LISTS, MimeTypes.MODULES_2C_MAP, null));
		vc.add(new Conversion(MimeTypes.ELEMENT_LISTS, MimeTypes.MODULES_INDEXED_MAP, null));

		vc.add(new Conversion(MimeTypes.MODULES_2C_MAP, MimeTypes.MODULES_INDEXED_MAP, new ModuleConversion()));
		vc.add(new Conversion(MimeTypes.MODULES_2C_MAP, MimeTypes.DOUBLE_MATRIX, null));
		vc.add(new Conversion(MimeTypes.MODULES_2C_MAP, MimeTypes.DOUBLE_BINARY_MATRIX, null));
		vc.add(new Conversion(MimeTypes.MODULES_2C_MAP, MimeTypes.ELEMENT_LISTS, null));

		vc.add(new Conversion(MimeTypes.MODULES_INDEXED_MAP, MimeTypes.MODULES_2C_MAP, new ModuleConversion()));
		vc.add(new Conversion(MimeTypes.MODULES_INDEXED_MAP, MimeTypes.DOUBLE_MATRIX, null));
		vc.add(new Conversion(MimeTypes.MODULES_INDEXED_MAP, MimeTypes.DOUBLE_BINARY_MATRIX, null));
		vc.add(new Conversion(MimeTypes.MODULES_INDEXED_MAP, MimeTypes.ELEMENT_LISTS, null));

		Conversion targetConv = new Conversion(args.inputFileMime, args.outputFileMime);
		int convIndex = vc.indexOf(targetConv);
		if (convIndex < 0)
			throw new ToolException("Unsupportted conversion from '"
					+ args.inputFileMime + "' to '" + args.outputFileMime + "'");

		targetConv = vc.get(convIndex);
		if (targetConv.delegate == null)
			throw new ToolException("Unimplemented conversion from '"
					+ args.inputFileMime + "' to '" + args.outputFileMime + "'");

		IProgressMonitor monitor = new StreamProgressMonitor(System.out, args.verbose, args.debug);

		monitor.begin("Loading input file ...", 1);

		File inputFile = new File(args.inputFileName);

		Object srcObject = null;
		try {
			srcObject = PersistenceManager.getDefault()
					.load(inputFile, args.inputFileMime, monitor.subtask());
		} catch (PersistenceException ex) {
			monitor.exception(ex);
			throw new ToolException(ex);
		}

		if (srcObject == null)
			throw new ToolException("Unexpected error loading " + args.inputFileName);

		monitor.end();

		Object dstObject = null;
		try {
			dstObject = targetConv.delegate
					.convert(args.inputFileMime, srcObject, args.outputFileMime, monitor);
		}
		catch (Exception ex) {
			monitor.exception(ex);
			throw new ToolException(ex);
		}

		monitor.begin("Saving output file ...", 1);

		File outputFile = new File(args.outputFileName);

		try {
			PersistenceManager.getDefault()
					.store(outputFile, args.outputFileMime, dstObject, monitor.subtask());
		} catch (PersistenceException ex) {
			monitor.exception(ex);
			throw new ToolException(ex);
		}

		monitor.end();
	}
}
