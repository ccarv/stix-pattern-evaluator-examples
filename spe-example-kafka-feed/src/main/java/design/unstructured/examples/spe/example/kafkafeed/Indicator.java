package design.unstructured.examples.spe.example.kafkafeed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import design.unstructured.stix.evaluator.Pattern;

/**
 * Indicator
 */
public class Indicator {

    private String name;

    private String description;

    private String pattern;

    private Pattern parsedPattern;

    private Boolean active = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public String getPattern() {
        return pattern;
    }

    @JsonProperty
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @JsonIgnore
    public Pattern getParsedPattern() {
        return parsedPattern;
    }

    public void setParsedPattern(Pattern parsedPattern) {
        this.parsedPattern = parsedPattern;
    }

    @JsonIgnore
    public Boolean getActive() {
        return active;
    }

    @JsonProperty
    public void setActive(Boolean active) {
        this.active = active;
    }
}
