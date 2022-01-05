
package org.pahappa.systems.core.utils.wp; ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "self",
    "collection",
    "about",
    "author",
    "replies",
    "version-history",
    "predecessor-version",
    "wp:featuredmedia",
    "wp:attachment",
    "wp:term",
    "curies"
})
@Generated("jsonschema2pojo")
public class Links {

    @JsonProperty("self")
    private List<Self> self = null;
    @JsonProperty("collection")
    private List<Collection> collection = null;
    @JsonProperty("about")
    private List<About> about = null;
    @JsonProperty("author")
    private List<Author> author = null;
    @JsonProperty("replies")
    private List<Reply> replies = null;
    @JsonProperty("version-history")
    private List<VersionHistory> versionHistory = null;
    @JsonProperty("predecessor-version")
    private List<PredecessorVersion> predecessorVersion = null;
    @JsonProperty("wp:featuredmedia")
    private List<WpFeaturedmedium> wpFeaturedmedia = null;
    @JsonProperty("wp:attachment")
    private List<WpAttachment> wpAttachment = null;
    @JsonProperty("wp:term")
    private List<WpTerm> wpTerm = null;
    @JsonProperty("curies")
    private List<Cury> curies = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("self")
    public List<Self> getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(List<Self> self) {
        this.self = self;
    }

    @JsonProperty("collection")
    public List<Collection> getCollection() {
        return collection;
    }

    @JsonProperty("collection")
    public void setCollection(List<Collection> collection) {
        this.collection = collection;
    }

    @JsonProperty("about")
    public List<About> getAbout() {
        return about;
    }

    @JsonProperty("about")
    public void setAbout(List<About> about) {
        this.about = about;
    }

    @JsonProperty("author")
    public List<Author> getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    @JsonProperty("replies")
    public List<Reply> getReplies() {
        return replies;
    }

    @JsonProperty("replies")
    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    @JsonProperty("version-history")
    public List<VersionHistory> getVersionHistory() {
        return versionHistory;
    }

    @JsonProperty("version-history")
    public void setVersionHistory(List<VersionHistory> versionHistory) {
        this.versionHistory = versionHistory;
    }

    @JsonProperty("predecessor-version")
    public List<PredecessorVersion> getPredecessorVersion() {
        return predecessorVersion;
    }

    @JsonProperty("predecessor-version")
    public void setPredecessorVersion(List<PredecessorVersion> predecessorVersion) {
        this.predecessorVersion = predecessorVersion;
    }

    @JsonProperty("wp:featuredmedia")
    public List<WpFeaturedmedium> getWpFeaturedmedia() {
        return wpFeaturedmedia;
    }

    @JsonProperty("wp:featuredmedia")
    public void setWpFeaturedmedia(List<WpFeaturedmedium> wpFeaturedmedia) {
        this.wpFeaturedmedia = wpFeaturedmedia;
    }

    @JsonProperty("wp:attachment")
    public List<WpAttachment> getWpAttachment() {
        return wpAttachment;
    }

    @JsonProperty("wp:attachment")
    public void setWpAttachment(List<WpAttachment> wpAttachment) {
        this.wpAttachment = wpAttachment;
    }

    @JsonProperty("wp:term")
    public List<WpTerm> getWpTerm() {
        return wpTerm;
    }

    @JsonProperty("wp:term")
    public void setWpTerm(List<WpTerm> wpTerm) {
        this.wpTerm = wpTerm;
    }

    @JsonProperty("curies")
    public List<Cury> getCuries() {
        return curies;
    }

    @JsonProperty("curies")
    public void setCuries(List<Cury> curies) {
        this.curies = curies;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
