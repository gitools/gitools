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

import org.gitools.persistence.formats.matrix.BdmMatrixFormat;
import org.gitools.persistence.formats.matrix.CdmMatrixFormat;
import org.gitools.persistence.formats.modulemap.TcmModuleMapFormat;
import org.gitools.utils.tools.exception.ToolException;

import java.util.ArrayList;
import java.util.List;

public class TestMain {

    public static void main(String[] unusedArgs) throws ToolException {
        String home = System.getProperty("user.home");
        String stuff = home + "/temp/gitools-stuff";
        String temp = home + "/temp/gitools";

        String cmd = "enrichment -t binomial-exact" +
                " -tc sample-size=100 -tc aproximation=none" +
                " -d " + stuff + "/d1.tsv" +
                " -df " + BdmMatrixFormat.EXTENSION +
                " -m " + stuff + "/m1.tsv" +
                " -mf " + TcmModuleMapFormat.EXTENSION +
                " -min 0" +
                " -w " + temp + "/test -N test" +
                " -title 'Test Analysis'" +
                " -notes 'Testing...'" +
                " -A author=Christian" +
                " -verbose -debug -err-log -";

        cmd = "overlapping" +
                " -d " + temp + "/test/data.cdm.gz" +
                " -df " + CdmMatrixFormat.EXTENSION +
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


    @SuppressWarnings("empty-statement")
    private static String[] cmdLineSplit(String cmd) {
        List<String> args = new ArrayList<>();

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
