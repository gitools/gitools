package org.gitools.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "title", "journal","pubmed",  "abstr","authors", "externalLink" })
public class Publication 
	implements Serializable {

	private static final long serialVersionUID = 4040601803383233010L;

	
	private String title;  
	private String journal;
	private String pubmed;
	
	private String abstr;
	
	//FIXME:
	@XmlTransient
	private Date year;
	
	
	@XmlElementWrapper(name = "authors")
    @XmlElement(name = "author")
    private List<Author> authors = new ArrayList<Author>();
	
	// private Reference linkPublication;
	private String externalLink;

	public Publication() {

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

	public void addAuthor(Author e) {
		this.authors.add(e);
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

	public String getExternalLink() {
		return externalLink;
	}

	public void setExternalLink(String externalLink) {
		this.externalLink = externalLink;
	}
}
