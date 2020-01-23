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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import design.unstructured.stix.evaluator.Pattern;
import design.unstructured.stix.evaluator.StixPatternProcessorException;

/**
 * Indicators
 */
@Service
public class Indicators extends ArrayList<Indicator> {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApplicationConfiguration configuration;

    @PostConstruct
    public void load() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        for (String fileString : configuration.getIndicatorFiles()) {
            this.addAll(Arrays.asList(jsonMapper.readValue(resourceLoader.getResource(fileString).getInputStream(), Indicator[].class)));
        }

        for (Indicator indicator : this) {
            try {
                indicator.setParsedPattern(Pattern.build(indicator.getPattern()));

            } catch (StixPatternProcessorException ex) {
                System.out.println("Disabled rule " + indicator.getName());
                indicator.setActive(false);
            }
        }
    }
}
