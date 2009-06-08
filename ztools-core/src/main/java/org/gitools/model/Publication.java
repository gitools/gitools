package org.gitools.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.gitools.model.REVIEW.Reference;

public class Publication 
		implements Serializable {

	private static final long serialVersionUID = 4040601803383233010L;

	private String abstr;
	private List<Author> authors = new ArrayList<Author>();
	private String journal;
	private String title;
	private Date year;
	private String pubmed;
	private Reference linkPublication;

	public Publication() {

	}

	public Publication(String title) {
		this.title = title;
	}

	public Publication(String title, List<Author> authors) {
		this.title = title;
		this.authors = authors;
	}	
	
	public Publication(String abstr, List<Author> authors, String journal,
			String title, Date year, String pubmed, Reference linkPublication) {
		super();
		this.abstr = abstr;
		this.authors = authors;
		this.journal = journal;
		this.title = title;
		this.year = year;
		this.pubmed = pubmed;
		this.linkPublication = linkPublication;
	}

	public String getAbstr() {
		return abstr;
	}

	public void setAbstr(String abstr) {
		this.abstr = abstr;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
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

	public Reference getLinkPublication() {
		return linkPublication;
	}

	public void setLinkPublication(Reference linkPublication) {
		this.linkPublication = linkPublication;
	}

	public void addAuthor(Author author) {
		this.authors.add(author);
	}

}
