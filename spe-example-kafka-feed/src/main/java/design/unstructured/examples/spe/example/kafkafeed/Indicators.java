package design.unstructured.examples.spe.example.kafkafeed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import design.unstructured.stix.evaluator.Pattern;
import design.unstructured.stix.evaluator.StixPatternProcessorException;

/**
 * Indicators
 */
@Component
public class Indicators extends ArrayList<Indicator> {

    private static final long serialVersionUID = 1L;

    private ResourceLoader resourceLoader;

    private KafkaFeedConfiguration configuration;

    @PostConstruct
    public void load() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        for (String fileString : configuration.getPatterns()) {
            this.addAll(Arrays.asList(jsonMapper.readValue(resourceLoader.getResource(fileString).getInputStream(), Indicator[].class)));
        }

        for (Indicator indicator : this) {
            try {
                indicator.setParsedPattern(Pattern.build(indicator.getPattern()));

            } catch (StixPatternProcessorException ex) {
                indicator.setActive(false);
            }
        }
    }

    @Autowired
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    public void setConfiguration(KafkaFeedConfiguration configuration) {
        this.configuration = configuration;
    }

}
