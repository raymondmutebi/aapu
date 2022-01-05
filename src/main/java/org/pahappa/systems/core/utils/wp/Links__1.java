
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
    "replies"
})
@Generated("jsonschema2pojo")
public class Links__1 {

    @JsonProperty("self")
    private List<Self__1> self = null;
    @JsonProperty("collection")
    private List<Collection__1> collection = null;
    @JsonProperty("about")
    private List<About__1> about = null;
    @JsonProperty("author")
    private List<Author__2> author = null;
    @JsonProperty("replies")
    private List<Reply__1> replies = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("self")
    public List<Self__1> getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(List<Self__1> self) {
        this.self = self;
    }

    @JsonProperty("collection")
    public List<Collection__1> getCollection() {
        return collection;
    }

    @JsonProperty("collection")
    public void setCollection(List<Collection__1> collection) {
        this.collection = collection;
    }

    @JsonProperty("about")
    public List<About__1> getAbout() {
        return about;
    }

    @JsonProperty("about")
    public void setAbout(List<About__1> about) {
        this.about = about;
    }

    @JsonProperty("author")
    public List<Author__2> getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(List<Author__2> author) {
        this.author = author;
    }

    @JsonProperty("replies")
    public List<Reply__1> getReplies() {
        return replies;
    }

    @JsonProperty("replies")
    public void setReplies(List<Reply__1> replies) {
        this.replies = replies;
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
