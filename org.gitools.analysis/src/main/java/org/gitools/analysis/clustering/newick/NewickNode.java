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
package org.gitools.analysis.clustering.newick;

import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class NewickNode<VT> {

    public enum Order {
        PRE_ORDER, POST_ORDER
    }

    private String name;
    private VT value;

    private final List<NewickNode> children;

    public NewickNode() {
        this(null, null);
    }

    private NewickNode(String name, VT value) {
        this.name = name;
        this.value = value;
        this.children = new ArrayList<>(2);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VT getValue() {
        return value;
    }

    public void setValue(VT value) {
        this.value = value;
    }

    public NewickNode getChild(int index) {
        return children.get(index);
    }

    public void setChild(int index, NewickNode node) {
        children.set(index, node);
    }

    public List<NewickNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public void addChild(NewickNode node) {
        children.add(node);
    }

    boolean isLeaf() {
        return children.isEmpty();
    }

    public int getMaxDepth() {
        return getMaxDepth(0);
    }

    int getMaxDepth(int depth) {
        int res = 0;

        if (isLeaf()) {
            return depth + 1;
        }

        for (NewickNode child : children) {
            int childDepth = child.getMaxDepth(depth + 1);
            if (childDepth > res) {
                res = childDepth;
            }
        }

        return res;
    }

    void leaves(List<NewickNode> leaves, int maxLevel, Order order) {
        if (children.isEmpty() || maxLevel == 0) {
            leaves.add(this);
        } else if (maxLevel > 0) {
            Iterable<NewickNode> iterable = null;
            switch (order) {
                case PRE_ORDER:
                    iterable = children;
                    break;
                case POST_ORDER:
                    iterable = Lists.reverse(children);
                    break;
            }
            for (NewickNode node : iterable)
                node.leaves(leaves, maxLevel - 1, order);
        }
    }


    List<NewickNode> getLeaves(int maxLevel, Order order) {
        List<NewickNode> leaves = new ArrayList<>();
        leaves(leaves, maxLevel, order);
        return leaves;
    }


    public List<NewickNode> getLeaves(int maxLevel) {
        return getLeaves(maxLevel, Order.PRE_ORDER);
    }


    public List<NewickNode> getLeaves() {
        return getLeaves(Integer.MAX_VALUE);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!children.isEmpty()) {
            sb.append('(').append(children.get(0));
            for (int i = 1; i < children.size(); i++)
                sb.append(',').append(children.get(i));
            sb.append(')');
        }
        if (name != null) {
            sb.append(name);
        }
        if (value != null) {
            sb.append(":").append(value);
        }
        return sb.toString();
    }
}
