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
package org.gitools.analysis.stats;

/**
 * Fisher's Exact Test
 * <p/>
 * Fisher's exact test is a statistical significance test used in
 * the analysis of categorical data where sample sizes are small.
 * <p/>
 * The test is used to examine the significance of the association
 * between two variables in a 2 x 2 contingency table.
 * <p/>
 * More info in http://en.wikipedia.org/wiki/Fisher's_exact_test
 */
public class FisherExactTest {

    private int a;
    private int b;
    private int c;
    private int d;

    private double leftPValue;
    private double rightPValue;
    private double twoTailPValue;


    private final HyperGeometric hyper = new HyperGeometric();

    /**
     * Constructor from an array contingency table:
     *
     * @param ctable ctable[0] = a, ctable[1] = b, ctable[2] = c, ctable[3] = d
     * @see FisherExactTest(int a, int b, int c, int d)
     */
    public FisherExactTest(int[] ctable) {
        this.a = ctable[0];
        this.b = ctable[1];
        this.c = ctable[2];
        this.d = ctable[3];
    }

    /**
     * Constructor from the contingency table:
     * <p/>
     * | x | !x|
     * ---+---+---+
     * y | a | b |
     * ---+---+---+
     * !y | c | d |
     * ---+---+---+
     *
     * @param a frequency of x ^ y
     * @param b frequency of !x ^ y
     * @param c frequency of x ^ !y
     * @param d frequency of !x ^ !y
     */
    public FisherExactTest(int a, int b, int c, int d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * Test from a given contingency table like:
     * <p/>
     * | x | !x|
     * ---+---+---+
     * y | a | b |
     * ---+---+---+
     * !y | c | d |
     * ---+---+---+
     *
     * @param a frequency of x ^ y
     * @param b frequency of !x ^ y
     * @param c frequency of x ^ !y
     * @param d frequency of !x ^ !y
     * @return probability = C(a+b, a) * C(c+d, c) / C(n, a+c)
     */
    public double testContingencyTable(int a, int b, int c, int d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;

        return testContingencyTable();
    }

    /**
     * Run Fisher's Exact Test using internal contingency table.
     *
     * @return probability = C(a+b, a) * C(c+d, c) / C(n, a+c)
     */
    public double testContingencyTable() {
        int n11 = a;
        int n1_ = a + b;
        int n_1 = a + c;
        int n = a + b + c + d;

        return internalTest(n11, n1_, n_1, n);
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

    public int getD() {
        return d;
    }

    public double getLeftPValue() {
        return leftPValue;
    }

    public double getRightPValue() {
        return rightPValue;
    }

    public double getTwoTailPValue() {
        return twoTailPValue;
    }

    /**
     * Adapted from http://www.langsrud.com/fisher.htm
     * Created by Oyvind Langsrud
     *
     * @param n11 = a
     * @param n1_ = a + b
     * @param n_1 = a + c
     * @param n   = a + b + c + d
     * @return probability = C(a+b, a) * C(c+d, c) / C(n, a+c)
     */
    private double internalTest(int n11, int n1_, int n_1, int n) {
        double sleft, sright, sless, slarg;
        double p, prob;
        int i, j;

        int max = n1_;
        if (n_1 < max) {
            max = n_1;
        }

        int min = n1_ + n_1 - n;
        if (min < 0) {
            min = 0;
        }

        if (min == max) {
            calcPValues(1.0, 1.0, 1.0, 1.0);
            return 1.0;
        }

        prob = hyper.hyper0(n11, n1_, n_1, n);

        sleft = 0.0;
        p = hyper.hyper(min);
        for (i = min + 1; p < 0.99999999 * prob; i++) {
            sleft += p;
            p = hyper.hyper(i);
        }
        i--;
        if (p < 1.00000001 * prob) {
            sleft += p;
        } else {
            i--;
        }

        sright = 0.0;
        p = hyper.hyper(max);
        for (j = max - 1; p < 0.99999999 * prob; j--) {
            sright += p;
            p = hyper.hyper(j);
        }
        j++;
        if (p < 1.00000001 * prob) {
            sright += p;
        } else {
            j++;
        }

        if (Math.abs(i - n11) < Math.abs(j - n11)) {
            sless = sleft;
            slarg = 1 - sleft + prob;
        } else {
            sless = 1 - sright + prob;
            slarg = sright;
        }

        calcPValues(sleft, sright, sless, slarg);

        return prob;
    }

    private void calcPValues(double sleft, double sright, double sless, double slarg) {

        leftPValue = sless < 1.0 ? sless : 1.0;
        rightPValue = slarg < 1.0 ? slarg : 1.0;
        twoTailPValue = sleft + sright;
        if (twoTailPValue > 1.0) {
            twoTailPValue = 1.0;
        }
    }
}
