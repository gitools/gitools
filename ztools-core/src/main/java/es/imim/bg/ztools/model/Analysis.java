package es.imim.bg.ztools.model;

import java.util.Date;

public class Analysis {

	protected String name;
	protected Date startTime;
	protected long elapsedTime;
	
	protected Data data;
	protected Modules modules;
	protected Results results;
	
	//protected TestFactory testFactory;
	protected TestConfig testConfig;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public long getElapsedTime() {
		return elapsedTime;
	}
	
	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
	
	public Data getData() {
		return data;
	}
	
	public void setData(Data data) {
		this.data = data;
	}
	
	public Modules getModules() {
		return modules;
	}
	
	public void setModules(Modules modules) {
		this.modules = modules;
	}
	
	public Results getResults() {
		return results;
	}
	
	public void setResults(Results results) {
		this.results = results;
	}
	
	public TestConfig getTestConfig() {
		return testConfig;
	}
	
	public void setTestConfig(TestConfig testConfig) {
		this.testConfig = testConfig;
	}
	
	/*public TestFactory getTestFactory() {
		return testFactory;
	}
	
	public void setTestFactory(TestFactory testFactory) {
		this.testFactory = testFactory;
	}*/
}
