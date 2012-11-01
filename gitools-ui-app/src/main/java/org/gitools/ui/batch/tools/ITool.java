package org.gitools.ui.batch.tools;

import java.io.PrintWriter;

public interface ITool {

    String getName();
    
    boolean run(String[] args, PrintWriter out);
    
    boolean check(String[] args, PrintWriter out);
    
}
