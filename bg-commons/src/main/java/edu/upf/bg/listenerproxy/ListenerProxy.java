/*
 *  Copyright 2009 Universitat Pompeu Fabra.
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

package edu.upf.bg.listenerproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ListenerProxy<L> implements InvocationHandler {

	private List<L> listeners = new ArrayList<L>();

	public void addListener(L listener) {
		listeners.add(listener);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		for (L listener : listeners) {
			try {
				/*Object result =*/ method.invoke(listener, args);
			}
			catch (InvocationTargetException ex) {
				throw ex.getTargetException();
			}
		}
		return null;
	}
}
