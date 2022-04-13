package com.umky.pdftoepub.epubmodel;

import com.umky.pdftoepub.epubmodel.metadata.Identifier;
import com.umky.pdftoepub.epubmodel.metadata.Meta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * [REQUIRED] first child of EPUBPackage
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EPUBMetadata {

    @XmlElement(name = "dc:identifier",required = true)
    private Identifier identifier = new Identifier();

    @XmlElement(name = "dc:language",required = true)
    private String language = "zh-CN";

    @XmlElement(name = "dc:title",required = true)
    private String title = "未知";

    //<meta property="dcterms:modified">2011-01-01T12:00:00Z</meta>
    @XmlElement(name = "opf:meta",required = true)
    private Meta lastModifiedMeta = new Meta();

    private String creator;
    private String subject;
    private String type;
    private String description;
    private String publisher;
    private String contributor;
    private String date;
    private String format;
    private String source;
    private String relation;
    private String coverage;
    private String rights;

    public EPUBMetadata() {
        lastModifiedMeta.setProperty("dcterms:modified");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        lastModifiedMeta.setValue(format.format(new Date()));
    }

    public Meta getLastModifiedMeta() {
        return lastModifiedMeta;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public void setLastModifiedDate(String date) {
        this.lastModifiedMeta.setValue(date);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }
}
