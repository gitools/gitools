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
package org.gitools.cli;

import org.gitools.core.persistence.formats.FileSuffixes;
import org.gitools.utils.tools.exception.ToolException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @noinspection ALL
 */
public class TestMain {

    public static void main(String[] unusedArgs) throws ToolException {
        String home = System.getProperty("user.home");
        String stuff = home + "/temp/gitools-stuff";
        String temp = home + "/temp/gitools";

        String cmd = "enrichment -t binomial-exact" +
                " -tc sample-size=100 -tc aproximation=none" +
                " -d " + stuff + "/d1.tsv" +
                " -df " + FileSuffixes.DOUBLE_BINARY_MATRIX +
                " -m " + stuff + "/m1.tsv" +
                " -mf " + FileSuffixes.MODULES_2C_MAP +
                " -min 0" +
                " -w " + temp + "/test -N test" +
                " -title 'Test Analysis'" +
                " -notes 'Testing...'" +
                " -A author=Christian" +
                " -verbose -debug -err-log -";

		/*cmd = "enrichment -t binomial-exact" +
                " -d " + stuff + "/real/data.tsv" +
				" -df " + MimeTypes.DOUBLE_MATRIX +
				" -b gt,0.05" +
				" -m " + stuff + "/real/modules.tsv" +
				" -mf tcm" + //MimeTypes.MODULES_2C_MAP +
				" -min 100" +
				" -w "+ temp + "/test -N test_gt" +
				" -title 'Test Analysis'" +
				" -notes 'Testing...'" +
				" -A author=Christian" +
				" -verbose -debug -err-log -";*/

		/*cmd = "oncodrive" +
                " -d " + stuff + "/GAFtargets_upDownGeneslog5.bdm" +
				" -min 0" +
				" -w " + temp +
				" -t binomial";*/

		/*cmd = "correlation" +
                " -d " + stuff + "/real/data.tsv" +
				" -dm " + MimeTypes.DOUBLE_MATRIX +
				" -w "+ temp + "/test -N test_cor" +
				" -verbose -debug -err-log -";*/

		/*cmd = "combination" +
                " -d " + stuff + "/brain-comb2.cdm.gz" +
				" -df " + MimeTypes.DOUBLE_MATRIX +
				" -w "+ temp + "/test-cmb -N test_comb" +
				" -verbose -debug -err-log -";*/

		/*String cmd = "convert" +
                " -i data.tsv" +
				" -im application/gitools-matrix-double" +
				" -o pru.txt" +
				" -om application/gitools-element-lists" +
				" -verbose -debug -err-log -";*/

		/*cmd = "convert" +
				" -o " + stuff + "/d2.tsv" +
				" -om " + MimeTypes.DOUBLE_BINARY_MATRIX +
				" -i " + stuff + "/data.gmx" +
				" -im " + MimeTypes.GENE_MATRIX +
				" -verbose -debug -err-log -";*/

        cmd = "overlapping" +
                " -d " + temp + "/test/data.cdm.gz" +
                " -df " + FileSuffixes.DOUBLE_MATRIX +
                " -b lt,0.05" +
                " -w " + temp + "/test -N test_ovl" +
                " -verbose -debug -err-log -";

        cmd = "oncodrive" +
                " -N upreg -w upreg -t binomial" +
                " -d /home/cperez/temp/intogen/v03_16/results/data/mrna/log2r_tumour_unit/4e0ddbf4-9d62-4836-b6d0-985aeb2b51e2.tsv.gz" +
                " -df cdm -b gt,1.174";

        String[] args = cmdLineSplit(cmd);

        Main.main(args);
    }

    @NotNull
    @SuppressWarnings("empty-statement")
    private static String[] cmdLineSplit(@NotNull String cmd) {
        List<String> args = new ArrayList<String>();

        int lastPos = 0;
        int pos = 0;
        while (pos < cmd.length()) {
            char ch = cmd.charAt(pos);
            switch (ch) {
                case ' ':
                    args.add(cmd.substring(lastPos, pos));
                    while (pos < cmd.length() && cmd.charAt(pos++) != ch)
                        ;
                    lastPos = pos;
                    break;
                case '"':
                case '\'':
                    lastPos = ++pos;
                    while (pos < cmd.length() && cmd.charAt(pos++) != ch)
                        ;
                    args.add(cmd.substring(lastPos, pos - 1));
                    while (pos < cmd.length() && cmd.charAt(pos) == ' ')
                        pos++;
                    lastPos = pos;
                    break;

                default:
                    pos++;
                    break;
            }
        }

        if (lastPos < cmd.length()) {
            args.add(cmd.substring(lastPos, pos));
        }

        String[] retArgs = new String[args.size()];
        args.toArray(retArgs);
        return retArgs;
    }
}
