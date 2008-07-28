package es.imim.bg.ztools.loaders;

import java.io.IOException;
import es.imim.bg.csv.CSVException;
import es.imim.bg.ztools.model.DataMatrix;

public interface DataLoader {

	public DataMatrix load() throws IOException, CSVException;

}