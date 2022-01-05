
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
    "medium",
    "large",
    "thumbnail",
    "medium_large",
    "intime-thumbnail",
    "intime-large",
    "intime-medium",
    "intime-service",
    "intime-history",
    "full"
})
@Generated("jsonschema2pojo")
public class Sizes {

    @JsonProperty("medium")
    private Medium medium;
    @JsonProperty("large")
    private Large large;
    @JsonProperty("thumbnail")
    private Thumbnail thumbnail;
    @JsonProperty("medium_large")
    private MediumLarge mediumLarge;
    @JsonProperty("intime-thumbnail")
    private IntimeThumbnail intimeThumbnail;
    @JsonProperty("intime-large")
    private IntimeLarge intimeLarge;
    @JsonProperty("intime-medium")
    private IntimeMedium intimeMedium;
    @JsonProperty("intime-service")
    private IntimeService intimeService;
    @JsonProperty("intime-history")
    private IntimeHistory intimeHistory;
    @JsonProperty("full")
    private Full full;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("medium")
    public Medium getMedium() {
        return medium;
    }

    @JsonProperty("medium")
    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    @JsonProperty("large")
    public Large getLarge() {
        return large;
    }

    @JsonProperty("large")
    public void setLarge(Large large) {
        this.large = large;
    }

    @JsonProperty("thumbnail")
    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    @JsonProperty("thumbnail")
    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    @JsonProperty("medium_large")
    public MediumLarge getMediumLarge() {
        return mediumLarge;
    }

    @JsonProperty("medium_large")
    public void setMediumLarge(MediumLarge mediumLarge) {
        this.mediumLarge = mediumLarge;
    }

    @JsonProperty("intime-thumbnail")
    public IntimeThumbnail getIntimeThumbnail() {
        return intimeThumbnail;
    }

    @JsonProperty("intime-thumbnail")
    public void setIntimeThumbnail(IntimeThumbnail intimeThumbnail) {
        this.intimeThumbnail = intimeThumbnail;
    }

    @JsonProperty("intime-large")
    public IntimeLarge getIntimeLarge() {
        return intimeLarge;
    }

    @JsonProperty("intime-large")
    public void setIntimeLarge(IntimeLarge intimeLarge) {
        this.intimeLarge = intimeLarge;
    }

    @JsonProperty("intime-medium")
    public IntimeMedium getIntimeMedium() {
        return intimeMedium;
    }

    @JsonProperty("intime-medium")
    public void setIntimeMedium(IntimeMedium intimeMedium) {
        this.intimeMedium = intimeMedium;
    }

    @JsonProperty("intime-service")
    public IntimeService getIntimeService() {
        return intimeService;
    }

    @JsonProperty("intime-service")
    public void setIntimeService(IntimeService intimeService) {
        this.intimeService = intimeService;
    }

    @JsonProperty("intime-history")
    public IntimeHistory getIntimeHistory() {
        return intimeHistory;
    }

    @JsonProperty("intime-history")
    public void setIntimeHistory(IntimeHistory intimeHistory) {
        this.intimeHistory = intimeHistory;
    }

    @JsonProperty("full")
    public Full getFull() {
        return full;
    }

    @JsonProperty("full")
    public void setFull(Full full) {
        this.full = full;
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
