/*
 *  Copyright 2010 cperez.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.idmapper;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MappingEngine {

	private static final Logger logger = LoggerFactory.getLogger(MappingEngine.class);

	private static class Edge {
		private MappingNode src;
		private MappingNode dst;
		private Mapper proc;

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
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof Edge))
				return false;
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

		@Override
		public String toString() {
			return src.toString() + " --> " + dst.toString();
		}
	}

	private static class Path {

		private LinkedList<Step> steps = new LinkedList<Step>();
		private Set<MappingNode> visited = new HashSet<MappingNode>();
		
		public Path() {
		}

		public Path(MappingNode node) {
			steps.add(new Step(node));
		}

		private Path(Path path, Step step) {
			steps.addAll(path.getSteps());
			steps.add(step);
		}

		public MappingNode getLastNode() {
			return steps.getLast().getNode();
		}
		
		public void addStep(Step step) {
			steps.add(step);
			visited.add(step.getNode());
		}

		public int getLength() {
			return steps.size() - 1;
		}

		public LinkedList<Step> getSteps() {
			return steps;
		}

		private boolean visited(MappingNode node) {
			return visited.contains(node);
		}

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
		private MappingNode node;
		private Mapper mapper;

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

		@Override
		public String toString() {
			return node.getId() + " {" + mapper.getName() + "}";
		}
	}

	private MappingContext context;

	private List<Edge> edges;

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

	public void addMapper(MappingNode src, MappingNode dst, Mapper proc) {
		edges.add(new Edge(src, dst, proc));
	}

	public MappingData run(String[] ids, String src, String dst, IProgressMonitor monitor) throws MappingException {
		monitor.begin("Mapping from " + src + " to " + dst + " ...", 4);

		MappingData data = new MappingData(src, src);
		if (ids != null)
			data.identity(new HashSet<String>(Arrays.asList(ids)));

		monitor.info("Searching mapping path ...");
		Path path = findPath(src, dst, monitor);
		if (path == null)
			throw new MappingException("Unable to find a mapping path from " + src + " to " + dst);

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

	public MappingData run(String src, String dst, IProgressMonitor monitor) throws MappingException {
		return run(null, src, dst, monitor);
	}

	private Path findPath(String src, String dst, IProgressMonitor monitor) {
		StringMappingNode dstNode = new StringMappingNode(dst);
		Path bestPath = null;
		int bestLength = Integer.MAX_VALUE;
		LinkedList<Path> paths = new LinkedList<Path>();
		paths.offer(new Path(new StringMappingNode(src)));
		while (paths.size() > 0) {
			Path path = paths.poll();
			if (path.getLength() >= bestLength)
				continue;

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
				}
				else if (!path.visited(step.getNode()))
					paths.add(new Path(path, step));
			}
		}
		return bestPath;
	}

	private List<Step> getSteps(MappingNode snode, boolean generatorRequired) {
		List<Step> steps = new ArrayList<Step>();

		for (Edge edge : edges) {
			MappingNode src = edge.getSrc();
			MappingNode dst = edge.getDst();
			Mapper mapper = edge.getMapper();
			
			if (generatorRequired && !mapper.isGenerator())
				continue;

			if (src.equals(snode))
				steps.add(new Step(dst, mapper));
			else if (mapper.isBidirectional() && dst.equals(snode))
				steps.add(new Step(src, mapper));
		}

		return steps;
	}
}
