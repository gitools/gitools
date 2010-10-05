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


public abstract class AbstractMapper implements Mapper {

	private String name;
	private boolean bidirectional;
	private boolean generator;

	public AbstractMapper(String name, boolean bidirectional, boolean generator) {
		this.name = name;
		this.bidirectional = bidirectional;
		this.generator = generator;
	}

	public AbstractMapper(String name) {
		this(name, false, false);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isBidirectional() {
		return bidirectional;
	}

	@Override
	public boolean isGenerator() {
		return generator;
	}

	@Override
	public String toString() {
		return name;
	}
}
