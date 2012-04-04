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

package org.gitools.analysis.htest;

import org.gitools.model.ToolConfig;
import org.gitools.stats.test.factory.TestFactory;

import org.gitools.analysis.AnalysisCommand;

public abstract class HtestCommand extends AnalysisCommand {

	protected static final char defaultSep = '\t';
	protected static final char defaultQuote = '"';

	protected HtestAnalysis analysis;

	protected String dataMime;
	protected String dataPath;
    protected int valueIndex;

	protected String populationPath;
	protected Double populationDefaultValue;

	public HtestCommand(
			HtestAnalysis analysis,
			String dataMime, String dataPath,
            int valueIndex,
			String populationPath,
			Double populationDefaultValue,
			String workdir, String fileName) {

		super(workdir, fileName);

		this.analysis = analysis;
		this.dataMime = dataMime;
		this.dataPath = dataPath;
        this.valueIndex = valueIndex;
		this.populationPath = populationPath;
		this.populationDefaultValue = populationDefaultValue;
	}

	public HtestAnalysis getaAnalysis() {
		return analysis;
	}

	public void setAnalysis(HtestAnalysis analysis) {
		this.analysis = analysis;
	}

	public String getDataMime() {
		return dataMime;
	}

	public void setDataMime(String dataMime) {
		this.dataMime = dataMime;
	}
	
	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

    public int getValueIndex() {
        return valueIndex;
    }

    public void setValueIndex(int valueIndex) {
        this.valueIndex = valueIndex;
    }

	public String getPopulationPath() {
		return populationPath;
	}

	public void setPopulationPath(String populationPath) {
		this.populationPath = populationPath;
	}
	
	protected TestFactory createTestFactory(String toolName, String configName) {		
		ToolConfig toolConfig =
			TestFactory.createToolConfig(toolName, configName);
		
		TestFactory testFactory = 
			TestFactory.createFactory(toolConfig);
		
		return testFactory;
	}
}
