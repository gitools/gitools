/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.app;

public class IconNamesColumns implements DimensionIcons {

    public static final String showAll16 = "/img/ColumnShowAll16.gif";
    public static final String showAll24 = "/img/ColumnShowAll24.gif";

    public static final String hide16 = "/img/ColumnHide16.gif";
    public static final String hide24 = "/img/ColumnHide24.gif";

    public static final String moveLeft16 = "/img/MoveColsLeft16.gif";
    public static final String moveLeft24 = "/img/MoveColsLeft24.gif";

    public static final String moveRight16 = "/img/MoveColsRight16.gif";
    public static final String moveRight24 = "/img/MoveColsRight24.gif";

    @Override
    public String getHide16() {
        return hide16;
    }

    @Override
    public String getHide24() {
        return hide24;
    }

    @Override
    public String getMoveBackward16() {
        return moveLeft16;
    }

    @Override
    public String getMoveBackward24() {
        return moveLeft24;
    }

    @Override
    public String getMoveForward16() {
        return moveRight16;
    }

    @Override
    public String getMoveForward24() {
        return moveRight24;
    }

    @Override
    public String getShowAll16() {
        return showAll16;
    }

    @Override
    public String getShowAll24() {
        return showAll24;
    }

}
