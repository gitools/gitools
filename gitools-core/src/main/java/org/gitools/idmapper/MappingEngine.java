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

	public MappingData run(String src, String dst, IProgressMonitor monitor) throws MappingException {
		monitor.begin("Mapping ...", 4);

		monitor.info("Searching mapping path ...");
		MappingData data = new MappingData(src, src);
		Path path = findPath(src, dst, monitor);
		monitor.debug("Mapping path: " + path);
		monitor.worked(1);

		LinkedList<Step> steps = path.getSteps();
		Iterator<Step> it = steps.iterator();

		monitor.info("Initializing mappers ...");
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
			data = step.getMapper().map(context, data, lastNode, step.getNode(), monitor.subtask());
			lastNode = step.getNode();
			data.setDstNode(lastNode);
		}
		monitor.worked(1);

		monitor.info("Finalizing mappers ...");
		for (Mapper m : initializedMappers)
			m.finalize(context, monitor);
		
		monitor.end();

		return data;
	}

	private Path findPath(String src, String dst, IProgressMonitor monitor) {
		StringMappingNode dstNode = new StringMappingNode(dst);
		Path bestPath = null;
		LinkedList<Path> paths = new LinkedList<Path>();
		paths.offer(new Path(new StringMappingNode(src)));
		while (paths.size() > 0) {
			Path path = paths.poll();
			MappingNode snode = path.getLastNode();
			List<Step> steps = getSteps(snode);
			for (Step step : steps) {
				if (step.getNode().equals(dstNode)) {
					path.addStep(step);
					if (bestPath == null || path.getLength() < bestPath.getLength())
						bestPath = path;
				}
				else if (!path.visited(step.getNode()))
					paths.add(new Path(path, step));
			}
		}
		return bestPath;
	}

	private List<Step> getSteps(MappingNode snode) {
		List<Step> steps = new ArrayList<Step>();

		for (Edge edge : edges)
			if (edge.getSrc().equals(snode))
				steps.add(new Step(edge.getDst(), edge.getMapper()));

		return steps;
	}
}
