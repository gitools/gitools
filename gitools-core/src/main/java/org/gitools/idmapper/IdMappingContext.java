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

import java.util.HashMap;


public class IdMappingContext extends HashMap<String, Object> {

	public String getString(String key) {
		return (String) get(key);
	}

	public void putString(String key, String value) {
		put(key, value);
	}

	public Integer getInteger(String key) {
		return (Integer) get(key);
	}

	public void putInteger(String key, Integer value) {
		put(key, value);
	}
}
