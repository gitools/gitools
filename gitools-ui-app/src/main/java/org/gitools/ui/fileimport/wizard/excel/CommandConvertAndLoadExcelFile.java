package org.gitools.ui.fileimport.wizard.excel;

import org.gitools.utils.csv.RawCsvWriter;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.apache.commons.io.FilenameUtils;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.ui.commands.CommandLoadFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CommandConvertAndLoadExcelFile extends CommandLoadFile {

    private int columns;
    private int rows;
    private List<Integer> values;
    private ExcelReader reader;

    public CommandConvertAndLoadExcelFile(int columns, int rows, List<Integer> values, ExcelReader reader) {
        super(createTdmFile(reader).getAbsolutePath(), FileFormats.MULTIVALUE_DATA_MATRIX.getMime());

        this.columns = columns;
        this.rows = rows;
        this.values = values;
        this.reader = reader;
    }

    private static File createTdmFile(ExcelReader reader) {
        File excelFile = reader.getFile();
        return new File( excelFile.getParent(), FilenameUtils.getBaseName( excelFile.getName() ) + ".tdm");
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {

        monitor.begin("Converting excel file", reader.getLastRowNum());

        File tdmFile = createTdmFile(reader);
        RawCsvWriter out = null;

        try {
            if (!tdmFile.exists()) {
                tdmFile.createNewFile();
            }
            out = new RawCsvWriter(new FileWriter( tdmFile ),'\t','"');
        } catch (IOException e) {
            throw new CommandException("Error opening file '" + tdmFile.getName() + "'");
        }

        // Write headers
        out.writeQuotedValue("column");
        out.writeSeparator();
        out.writeQuotedValue("row");
        for (int v : values) {
            out.writeSeparator();
            out.writeQuotedValue(reader.getValue(0, v));
        }
        out.writeNewLine();

        // Write rows
        for (int r=1; r <= reader.getLastRowNum(); r++) {
            out.writeQuotedValue(reader.getValue(r, columns));
            out.writeSeparator();
            out.writeQuotedValue(reader.getValue(r, rows));

            for (int v : values) {
                out.writeSeparator();
                String value = reader.getValue(r, v);
                out.writeValue((value != null ? value : "-"));
            }
            out.writeNewLine();
            monitor.worked(r);

            if (monitor.isCancelled()) {
                out.close();
                tdmFile.delete();
                return;
            }
        }
        out.close();

        super.execute(monitor);
    }
}
