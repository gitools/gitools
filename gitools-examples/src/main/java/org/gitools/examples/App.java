package org.gitools.examples;

import edu.upf.bg.progressmonitor.StreamProgressMonitor;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println(
				ExamplesManager.getDefault()
					.resolvePath("combination",
					new StreamProgressMonitor(System.out, true, true)));
    }
}
