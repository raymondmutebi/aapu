
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
    "author",
    "wp:featuredmedia",
    "wp:term"
})
@Generated("jsonschema2pojo")
public class Embedded {

    @JsonProperty("author")
    private List<Author__1> author = null;
    @JsonProperty("wp:featuredmedia")
    private List<WpFeaturedmedium__1> wpFeaturedmedia = null;
    @JsonProperty("wp:term")
    private List<List<WpTerm__1>> wpTerm = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("author")
    public List<Author__1> getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(List<Author__1> author) {
        this.author = author;
    }

    @JsonProperty("wp:featuredmedia")
    public List<WpFeaturedmedium__1> getWpFeaturedmedia() {
        return wpFeaturedmedia;
    }

    @JsonProperty("wp:featuredmedia")
    public void setWpFeaturedmedia(List<WpFeaturedmedium__1> wpFeaturedmedia) {
        this.wpFeaturedmedia = wpFeaturedmedia;
    }

    @JsonProperty("wp:term")
    public List<List<WpTerm__1>> getWpTerm() {
        return wpTerm;
    }

    @JsonProperty("wp:term")
    public void setWpTerm(List<List<WpTerm__1>> wpTerm) {
        this.wpTerm = wpTerm;
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
