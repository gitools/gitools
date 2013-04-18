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
package org.gitools.newick;

import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


@XmlAccessorType(XmlAccessType.FIELD)
public class NewickTree<VT> {

    private NewickNode<VT> root;

    public NewickTree() {
    }

    public NewickNode<VT> getRoot() {
        return root;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void setRoot(NewickNode<VT> root) {
        this.root = root;
    }

    public int getDepth() {
        return root.getMaxDepth(0);
    }

    @NotNull
    @Override
    public String toString() {
        return root.toString() + ";";
    }
}
