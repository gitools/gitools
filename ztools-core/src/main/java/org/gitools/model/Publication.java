package org.gitools.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Publication 
		implements Serializable {

	private static final long serialVersionUID = 4040601803383233010L;

	private String abstr;
	private List<Author> authors = new ArrayList<Author>();
	private String journal;
	private String title;
	private Date year;
	private String pubmed;
	//private Reference linkPublication;
	private String externalLink;

	public Publication() {

	}

	public Publication(String title) {
		this.title = title;
	}

	public Publication(String title, List<Author> authors) {
		this.title = title;
		this.authors = authors;
	}	
	
	public Publication(
			String abstr, List<Author> authors, String journal,
			String title, Date year, String pubmed, 
			String externalLink) {
		
		this.abstr = abstr;
		this.authors = authors;
		this.journal = journal;
		this.title = title;
		this.year = year;
		this.pubmed = pubmed;
		//this.linkPublication = linkPublication;
		this.externalLink = externalLink;
	}

	public String getAbstract() {
		return abstr;
	}

	public void setAbstract(String abstr) {
		this.abstr = abstr;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public void addAuthor(Author author) {
		this.authors.add(author);
	}
	
	public String getJournal() {
		return journal;
	}

	public void setJournal(String journal) {
		this.journal = journal;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getYear() {
		return year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public String getPubmed() {
		return pubmed;
	}

	public void setPubmed(String pubmed) {
		this.pubmed = pubmed;
	}

	/*public Reference getLinkPublication() {
		return linkPublication;
	}

	public void setLinkPublication(Reference linkPublication) {
		this.linkPublication = linkPublication;
	}*/

	public String getExternalLink() {
		return externalLink;
	}
	
	public void setExternalLink(String externalLink) {
		this.externalLink = externalLink;
	}
}
