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
package org.gitools.core.matrix.model;

import com.google.common.base.Predicate;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public interface IMatrixViewDimension extends IMatrixDimension {

    List<String> toList();

    void sort(Comparator<String> comparator);

    public void move(Direction direction, Set<String> indices);

    void show(List<String> indices);

    void show(Predicate<String> predicate);

    void showAll();

    void hide(Set<String> indices);

    void hide(Predicate<String> predicate);

    Set<String> getSelected();

    void select(Set<String> selected);

    void select(Predicate<String> predicate);

    void selectAll();

    String getFocus();

    void setFocus(String identifier);


}
