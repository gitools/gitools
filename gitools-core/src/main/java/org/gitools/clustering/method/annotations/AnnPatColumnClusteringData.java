/*
 *  Copyright 2011 chris.
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

package org.gitools.clustering.method.annotations;

import edu.upf.bg.textpatt.TextPattern;
import edu.upf.bg.textpatt.TextPattern.VariableValueResolver;
import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringDataInstance;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.AnnotationResolver;
import org.gitools.matrix.model.IMatrix;

public class AnnPatColumnClusteringData implements ClusteringData {

	public class Instance implements ClusteringDataInstance {

		private VariableValueResolver resolver;

		public Instance(VariableValueResolver resolver) {
			this.resolver = resolver;
		}

		@Override
		public int getNumAttributes() {
			return 1;
		}

		@Override
		public String getAttributeName(int attribute) {
			return "value";
		}

		@Override
		public Class<?> getValueClass(int attribute) {
			return String.class;
		}

		@Override
		public Object getValue(int attribute) {
			return pat.generate(resolver);
		}

		@Override
		public <T> T getTypedValue(int attribute, Class<T> valueClass) {
			if (!String.class.equals(valueClass))
				throw new RuntimeException("Unsupported value class: " + valueClass.getName());

			return (T) getValue(attribute);
		}
	}

	private IMatrix matrix;
	private AnnotationMatrix am;
	private TextPattern pat;

	public AnnPatColumnClusteringData(IMatrix matrix, AnnotationMatrix am, String pattern) {
		this.matrix = matrix;
		this.am = am;
		this.pat = new TextPattern(pattern);
	}

	@Override
	public int getSize() {
		return matrix.getColumnCount();
	}

	@Override
	public String getLabel(int index) {
		return matrix.getColumnLabel(index);
	}

	@Override
	public ClusteringDataInstance getInstance(int index) {
		return new Instance(
				new AnnotationResolver(
					am, matrix.getColumnLabel(index), "N/A"));
	}
}
