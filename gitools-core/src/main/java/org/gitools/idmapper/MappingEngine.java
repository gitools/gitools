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
package org.gitools.idmapper;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MappingEngine {

    private static final Logger logger = LoggerFactory.getLogger(MappingEngine.class);

    private static class Edge {
        private final MappingNode src;
        private final MappingNode dst;
        private final Mapper proc;

        public Edge(MappingNode src, MappingNode dst, Mapper proc) {
            this.src = src;
            this.dst = dst;
            this.proc = proc;
        }

        public MappingNode getSrc() {
            return src;
        }

        public MappingNode getDst() {
            return dst;
        }

        public Mapper getMapper() {
            return proc;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (obj == null || !(obj instanceof Edge)) {
                return false;
            }
            Edge other = (Edge) obj;

            return src.equals(other.getSrc()) && dst.equals(other.getDst());
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 67 * hash + (this.src != null ? this.src.hashCode() : 0);
            hash = 67 * hash + (this.dst != null ? this.dst.hashCode() : 0);
            return hash;
        }

        @NotNull
        @Override
        public String toString() {
            return src.toString() + " --> " + dst.toString();
        }
    }

    private static class Path {

        @NotNull
        private final LinkedList<Step> steps = new LinkedList<Step>();
        @NotNull
        private final Set<MappingNode> visited = new HashSet<MappingNode>();

        public Path() {
        }

        public Path(MappingNode node) {
            steps.add(new Step(node));
        }

        private Path(@NotNull Path path, Step step) {
            steps.addAll(path.getSteps());
            steps.add(step);
        }

        public MappingNode getLastNode() {
            return steps.getLast().getNode();
        }

        public void addStep(@NotNull Step step) {
            steps.add(step);
            visited.add(step.getNode());
        }

        public int getLength() {
            return steps.size() - 1;
        }

        @NotNull
        public LinkedList<Step> getSteps() {
            return steps;
        }

        private boolean visited(MappingNode node) {
            return visited.contains(node);
        }

        @NotNull
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            Iterator<Step> it = steps.iterator();
            if (it.hasNext()) {
                Step step = it.next();
                sb.append(step.getNode());
                while (it.hasNext()) {
                    step = it.next();
                    sb.append(" --[").append(step.getMapper());
                    sb.append("]--> ").append(step.getNode());
                }
            }
            return sb.toString();
        }
    }

    private static class Step {
        private final MappingNode node;
        private final Mapper mapper;

        public Step(MappingNode node) {
            this(node, null);
        }

        public Step(MappingNode node, Mapper mapper) {
            this.node = node;
            this.mapper = mapper;
        }

        public MappingNode getNode() {
            return node;
        }

        public Mapper getMapper() {
            return mapper;
        }

        @NotNull
        @Override
        public String toString() {
            return node.getId() + " {" + mapper.getName() + "}";
        }
    }

    private final MappingContext context;

    private final List<Edge> edges;

    public MappingEngine() {
        this.context = new MappingContext();
        this.edges = new ArrayList<Edge>();
    }

    public MappingContext getContext() {
        return context;
    }

    public void addMapper(String src, String dst, Mapper proc) {
        addMapper(new StringMappingNode(src), new StringMappingNode(dst), proc);
    }

    void addMapper(MappingNode src, MappingNode dst, Mapper proc) {
        edges.add(new Edge(src, dst, proc));
    }

    public MappingData run(@Nullable String[] ids, String src, String dst, @NotNull IProgressMonitor monitor) throws MappingException {
        monitor.begin("Mapping from " + src + " to " + dst + " ...", 4);

        MappingData data = new MappingData(src, src);
        if (ids != null) {
            data.identity(new HashSet<String>(Arrays.asList(ids)));
        }

        monitor.info("Searching mapping path ...");
        Path path = findPath(src, dst, monitor);
        if (path == null) {
            throw new MappingException("Unable to find a mapping path from " + src + " to " + dst);
        }

        monitor.debug("Mapping path: " + path);
        monitor.worked(1);

        LinkedList<Step> steps = path.getSteps();
        Iterator<Step> it = steps.iterator();

        Set<Mapper> initializedMappers = new HashSet<Mapper>();
        it.next();
        while (it.hasNext()) {
            Step step = it.next();
            Mapper mapper = step.getMapper();
            if (!initializedMappers.contains(mapper)) {
                mapper.initialize(context, monitor);
                initializedMappers.add(mapper);
            }
        }
        monitor.worked(1);

        it = steps.iterator();
        MappingNode lastNode = it.next().getNode();
        while (it.hasNext()) {
            Step step = it.next();
            IProgressMonitor m = monitor.subtask();
            m.begin("Mapping from " + lastNode + " to " + step.getNode() + " ...", 1);

            data = step.getMapper().map(context, data, lastNode, step.getNode(), m.subtask());
            lastNode = step.getNode();
            data.setDstNode(lastNode);
            data.removeEmptyKeys();

            m.end();
        }
        monitor.worked(1);

        for (Mapper m : initializedMappers)
            m.finalize(context, monitor);

        monitor.end();

        return data;
    }

    public MappingData run(String src, String dst, @NotNull IProgressMonitor monitor) throws MappingException {
        return run(null, src, dst, monitor);
    }

    @Nullable
    private Path findPath(String src, String dst, IProgressMonitor monitor) {
        StringMappingNode dstNode = new StringMappingNode(dst);
        Path bestPath = null;
        int bestLength = Integer.MAX_VALUE;
        LinkedList<Path> paths = new LinkedList<Path>();
        paths.offer(new Path(new StringMappingNode(src)));
        while (paths.size() > 0) {
            Path path = paths.poll();
            if (path.getLength() >= bestLength) {
                continue;
            }

            MappingNode snode = path.getLastNode();
            boolean generatorRequired = path.getLength() == 0;
            List<Step> steps = getSteps(snode, generatorRequired);
            for (Step step : steps) {
                if (step.getNode().equals(dstNode)) {
                    if (bestPath == null) {
                        bestPath = new Path(path, step);
                        bestLength = bestPath.getLength();
                        break;
                    }
                } else if (!path.visited(step.getNode())) {
                    paths.add(new Path(path, step));
                }
            }
        }
        return bestPath;
    }

    @NotNull
    private List<Step> getSteps(MappingNode snode, boolean generatorRequired) {
        List<Step> steps = new ArrayList<Step>();

        for (Edge edge : edges) {
            MappingNode src = edge.getSrc();
            MappingNode dst = edge.getDst();
            Mapper mapper = edge.getMapper();

            if (generatorRequired && !mapper.isGenerator()) {
                continue;
            }

            if (src.equals(snode)) {
                steps.add(new Step(dst, mapper));
            } else if (mapper.isBidirectional() && dst.equals(snode)) {
                steps.add(new Step(src, mapper));
            }
        }

        return steps;
    }
}
