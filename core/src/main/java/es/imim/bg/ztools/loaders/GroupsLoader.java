package es.imim.bg.ztools.loaders;

import java.io.IOException;

import es.imim.bg.csv.CSVException;
import es.imim.bg.ztools.model.Groups;

public interface GroupsLoader {

	public Groups load() throws IOException, CSVException;
}