/*
 *  Copyright 2010 Universitat Pompeu Fabra.
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

package org.gitools.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Publication 
	implements Serializable {

	private static final long serialVersionUID = 4040601803383233010L;

	private String title;
	private String authors;
	private String abstr;
	private String journal;
	private String pubmed;
	private String url;

	public Publication() {
	}

	public Publication(String title, String authors, String abstr,
			String journal, String pubmed, String url) {
		this.title = title;
		this.authors = authors;
		this.abstr = abstr;
		this.journal = journal;
		this.pubmed = pubmed;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthors() {
		return authors;
	}
	
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	
	public String getAbstract() {
		return abstr;
	}

	public void setAbstract(String abstr) {
		this.abstr = abstr;
	}

	public String getJournal() {
		return journal;
	}
	
	public void setJournal(String journal) {
		this.journal = journal;
	}
	
	public String getPubmed() {
		return pubmed;
	}
	
	public void setPubmed(String pubmed) {
		this.pubmed = pubmed;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
