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

import java.util.ArrayList;
import java.util.List;


public class IdMappingEngine {

	private static class Edge {
		private IdMappingNode src;
		private IdMappingNode dst;
		private IdMappingProcessor proc;

		public Edge(IdMappingNode src, IdMappingNode dst, IdMappingProcessor proc) {
			this.src = src;
			this.dst = dst;
			this.proc = proc;
		}

		public IdMappingNode getSrc() {
			return src;
		}

		public IdMappingNode getDst() {
			return dst;
		}

		public IdMappingProcessor getProc() {
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

	private IdMappingContext context;

	private List<Edge> edges;

	public IdMappingEngine() {
		this.edges = new ArrayList<Edge>();
	}

	public IdMappingContext getContext() {
		return context;
	}

	public void addProcessor(IdMappingNode src, IdMappingNode dst, IdMappingProcessor proc) {
		edges.add(new Edge(src, dst, proc));
	}
}
