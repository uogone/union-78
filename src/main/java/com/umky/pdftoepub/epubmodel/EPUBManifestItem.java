package com.umky.pdftoepub.epubmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class EPUBManifestItem {

    @XmlAttribute(required = true)
    private String id;

    @XmlAttribute(required = true)
    private String href;

    @XmlAttribute(name = "media-type",required = true)
    private String mediaType;

    //conditionally required
    @XmlAttribute
    private String fallback;

    @XmlAttribute
    private String properties;

    public EPUBManifestItem(String id,String href,String mediaType) {
        setId(id);
        setMediaType(mediaType);
        setHref(href);
    }

    public EPUBManifestItem() {
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getFallback() {
        return fallback;
    }

    public void setFallback(String fallback) {
        this.fallback = fallback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }
}
