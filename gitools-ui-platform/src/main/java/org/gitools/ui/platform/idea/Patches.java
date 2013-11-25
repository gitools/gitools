package org.gitools.ui.platform.idea;

import org.gitools.ui.platform.os.SystemInfo;

/**
 * Created with IntelliJ IDEA.
 * User: jordi
 * Date: 25/11/13
 * Time: 06:04
 * To change this template use File | Settings | File Templates.
 */
public class Patches {

    public static final boolean SUN_BUG_ID_7172665 = SystemInfo.isXWindow && SystemInfo.isJavaVersionAtLeast("1.7");

}
