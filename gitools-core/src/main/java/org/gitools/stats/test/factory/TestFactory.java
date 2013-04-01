/*
 * #%L
 * gitools-core
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
package org.gitools.stats.test.factory;

import org.gitools.model.ToolConfig;
import org.gitools.stats.test.Test;

import java.util.HashMap;
import java.util.Map;

public abstract class TestFactory
{

    public static final String TEST_NAME_PROPERTY = "test-name";

    public static final String ZSCORE_TEST = "zscore";
    public static final String BINOMIAL_TEST = "binomial";
    public static final String FISHER_EXACT_TEST = "fisher-exact";
    public static final String HYPERGEOMETRIC_TEST = "hypergeometric";
    public static final String CHI_SQUARE_TEST = "chi-square";
    public static final String MANN_WHITNEY_WILCOXON = "mannWhitneyWilcoxon";

    private static enum TestEnum
    {
        zscore, binomial, hypergeometric, fisherExact, chiSquare, mannWhitneyWilcoxon
    }

    private static final Map<String, TestEnum> testNameMap =
            new HashMap<String, TestEnum>();

    static
    {
        testNameMap.put(ZSCORE_TEST, TestEnum.zscore);
        testNameMap.put(BINOMIAL_TEST, TestEnum.binomial);
        testNameMap.put(FISHER_EXACT_TEST, TestEnum.fisherExact);
        testNameMap.put(HYPERGEOMETRIC_TEST, TestEnum.hypergeometric);
        testNameMap.put(CHI_SQUARE_TEST, TestEnum.chiSquare);
        testNameMap.put(MANN_WHITNEY_WILCOXON, TestEnum.mannWhitneyWilcoxon);
    }

    private static enum TestConfigEnum
    {
        zscoreMean, zscoreMedian,
        binomial, binomialExact, binomialNormal, binomialPoisson,
        hypergeometric, fisherExact, chiSquare, mannWhitneyWilcoxon
    }

    private static Map<String, TestConfigEnum> testAliases =
            new HashMap<String, TestConfigEnum>();

    static
    {
        testAliases.put("zscore", TestConfigEnum.zscoreMean);
        testAliases.put("zscore-mean", TestConfigEnum.zscoreMean);
        testAliases.put("zscore-median", TestConfigEnum.zscoreMedian);
        testAliases.put("binomial", TestConfigEnum.binomialExact);
        testAliases.put("binomial-exact", TestConfigEnum.binomialExact);
        testAliases.put("binomial-normal", TestConfigEnum.binomialNormal);
        testAliases.put("binomial-poisson", TestConfigEnum.binomialPoisson);
        testAliases.put("fisher", TestConfigEnum.fisherExact);
        testAliases.put("hyper-geom", TestConfigEnum.hypergeometric);
        testAliases.put("hyper-geometric", TestConfigEnum.hypergeometric);
        testAliases.put("hypergeometric", TestConfigEnum.hypergeometric);
        testAliases.put("chi-square", TestConfigEnum.chiSquare);
        testAliases.put("mannWhitneyWilcoxon", TestConfigEnum.mannWhitneyWilcoxon);
    }

    public static TestFactory createFactory(ToolConfig config)
    {

        final String testName = config.get(TEST_NAME_PROPERTY);
        TestEnum selectedTest = testNameMap.get(testName);
        if (selectedTest == null)
        {
            throw new IllegalArgumentException("Unknown test " + testName);
        }

        TestFactory testFactory = null;

        switch (selectedTest)
        {
            case zscore:
                testFactory = new ZscoreTestFactory(config);
                break;
            case binomial:
                testFactory = new BinomialTestFactory(config);
                break;
            case fisherExact:
                testFactory = new FisherTestFactory(config);
                break;
            case hypergeometric:
                throw new IllegalArgumentException("Test not implemented yet: " + testName);
                //break;
            case chiSquare:
                throw new IllegalArgumentException("Test not implemented yet: " + testName);
                //break;
            case mannWhitneyWilcoxon:
                testFactory = new MannWhitneyWilcoxonTestFactory(config);
        }

        return testFactory;
    }

    public static ToolConfig createToolConfig(String toolName, String configName)
    {

        TestConfigEnum selectedTest = testAliases.get(configName);
        if (selectedTest == null)
        {
            throw new IllegalArgumentException("Unknown test " + configName);
        }

        ToolConfig config = new ToolConfig(toolName);

        switch (selectedTest)
        {
            case zscoreMean:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.ZSCORE_TEST);
                config.put(
                        ZscoreTestFactory.NUM_SAMPLES_PROPERTY,
                        String.valueOf(ZscoreTestFactory.DEFAULT_NUM_SAMPLES));
                config.put(
                        ZscoreTestFactory.ESTIMATOR_PROPERTY,
                        ZscoreTestFactory.MEAN_ESTIMATOR);
                break;
            case zscoreMedian:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.ZSCORE_TEST);
                config.put(
                        ZscoreTestFactory.NUM_SAMPLES_PROPERTY,
                        String.valueOf(ZscoreTestFactory.DEFAULT_NUM_SAMPLES));
                config.put(
                        ZscoreTestFactory.ESTIMATOR_PROPERTY,
                        ZscoreTestFactory.MEDIAN_ESTIMATOR);
                break;
            case binomial:
            case binomialExact:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.BINOMIAL_TEST);
                config.put(
                        BinomialTestFactory.APROXIMATION_PROPERTY,
                        BinomialTestFactory.EXACT_APROX);
                break;
            case binomialNormal:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.BINOMIAL_TEST);
                config.put(
                        BinomialTestFactory.APROXIMATION_PROPERTY,
                        BinomialTestFactory.NORMAL_APROX);
                break;
            case binomialPoisson:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.BINOMIAL_TEST);
                config.put(
                        BinomialTestFactory.APROXIMATION_PROPERTY,
                        BinomialTestFactory.POISSON_APROX);
                break;
            case hypergeometric:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.HYPERGEOMETRIC_TEST);
                break;
            case fisherExact:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.FISHER_EXACT_TEST);
                break;
            case chiSquare:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.CHI_SQUARE_TEST);
                break;
            case mannWhitneyWilcoxon:
                config.put(TestFactory.TEST_NAME_PROPERTY, TestFactory.MANN_WHITNEY_WILCOXON);
                break;
        }

        return config;
    }

    protected ToolConfig toolConfig;

    public TestFactory(ToolConfig config)
    {
        this.toolConfig = config;
    }

    public ToolConfig getTestConfig()
    {
        return toolConfig;
    }

    public abstract Test create();
}
