
package org.pahappa.systems.core.utils.wp; ;

import java.util.HashMap;
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
    "id",
    "date",
    "slug",
    "type",
    "link",
    "title",
    "author",
    "caption",
    "alt_text",
    "media_type",
    "mime_type",
    "media_details",
    "source_url",
    "_links"
})
@Generated("jsonschema2pojo")
public class WpFeaturedmedium__1 {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("date")
    private String date;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("type")
    private String type;
    @JsonProperty("link")
    private String link;
    @JsonProperty("title")
    private Title__1 title;
    @JsonProperty("author")
    private Integer author;
    @JsonProperty("caption")
    private Caption caption;
    @JsonProperty("alt_text")
    private String altText;
    @JsonProperty("media_type")
    private String mediaType;
    @JsonProperty("mime_type")
    private String mimeType;
    @JsonProperty("media_details")
    private MediaDetails mediaDetails;
    @JsonProperty("source_url")
    private String sourceUrl;
    @JsonProperty("_links")
    private Links__1 links;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    @JsonProperty("slug")
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("title")
    public Title__1 getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(Title__1 title) {
        this.title = title;
    }

    @JsonProperty("author")
    public Integer getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(Integer author) {
        this.author = author;
    }

    @JsonProperty("caption")
    public Caption getCaption() {
        return caption;
    }

    @JsonProperty("caption")
    public void setCaption(Caption caption) {
        this.caption = caption;
    }

    @JsonProperty("alt_text")
    public String getAltText() {
        return altText;
    }

    @JsonProperty("alt_text")
    public void setAltText(String altText) {
        this.altText = altText;
    }

    @JsonProperty("media_type")
    public String getMediaType() {
        return mediaType;
    }

    @JsonProperty("media_type")
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    @JsonProperty("mime_type")
    public String getMimeType() {
        return mimeType;
    }

    @JsonProperty("mime_type")
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @JsonProperty("media_details")
    public MediaDetails getMediaDetails() {
        return mediaDetails;
    }

    @JsonProperty("media_details")
    public void setMediaDetails(MediaDetails mediaDetails) {
        this.mediaDetails = mediaDetails;
    }

    @JsonProperty("source_url")
    public String getSourceUrl() {
        return sourceUrl;
    }

    @JsonProperty("source_url")
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @JsonProperty("_links")
    public Links__1 getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Links__1 links) {
        this.links = links;
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
