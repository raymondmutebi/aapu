
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
    "wp:post_type",
    "curies"
})
@Generated("jsonschema2pojo")
public class Links__2 {

    @JsonProperty("self")
    private List<Self__2> self = null;
    @JsonProperty("collection")
    private List<Collection__2> collection = null;
    @JsonProperty("about")
    private List<About__2> about = null;
    @JsonProperty("wp:post_type")
    private List<WpPostType> wpPostType = null;
    @JsonProperty("curies")
    private List<Cury__1> curies = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("self")
    public List<Self__2> getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(List<Self__2> self) {
        this.self = self;
    }

    @JsonProperty("collection")
    public List<Collection__2> getCollection() {
        return collection;
    }

    @JsonProperty("collection")
    public void setCollection(List<Collection__2> collection) {
        this.collection = collection;
    }

    @JsonProperty("about")
    public List<About__2> getAbout() {
        return about;
    }

    @JsonProperty("about")
    public void setAbout(List<About__2> about) {
        this.about = about;
    }

    @JsonProperty("wp:post_type")
    public List<WpPostType> getWpPostType() {
        return wpPostType;
    }

    @JsonProperty("wp:post_type")
    public void setWpPostType(List<WpPostType> wpPostType) {
        this.wpPostType = wpPostType;
    }

    @JsonProperty("curies")
    public List<Cury__1> getCuries() {
        return curies;
    }

    @JsonProperty("curies")
    public void setCuries(List<Cury__1> curies) {
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
