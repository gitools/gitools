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

package org.gitools.persistence;

import org.junit.Test;
import static org.junit.Assert.*;

public class PersistenceUtilsTest {

	@Test
	public void baseName() {
		String baseName = PersistenceUtils.getFileName("/path/file.ext1.ext2");
		assertEquals("file.ext1", baseName);
	}
	
    @Test
    public void forwardRelativePath() {
		String basePath = "/base";
		String targetPath = "/base/target/file";
		String relPath = PersistenceUtils.getRelativePath(basePath, targetPath);
		assertEquals("target/file", relPath);
	}

	@Test
    public void backwardRelativePath() {
		String basePath = "/base/path";
		String targetPath = "/base/file";
		String relPath = PersistenceUtils.getRelativePath(basePath, targetPath);
		assertEquals("../file", relPath);
	}
}