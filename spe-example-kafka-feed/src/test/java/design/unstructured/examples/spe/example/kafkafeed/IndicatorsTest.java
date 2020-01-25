package design.unstructured.examples.spe.example.kafkafeed;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * IndicatorsTest
 */
public class IndicatorsTest {

    Indicators indicators;

    @BeforeEach
    public void configureResourceLoader() {
        indicators = new Indicators();
        indicators.setResourceLoader(new DefaultResourceLoader());
    }

    @Test
    public void loadIndicatorsFromClassPath() throws JsonParseException, JsonMappingException, IOException {
        KafkaFeedConfiguration config =
                new KafkaFeedConfiguration(null, Arrays.asList("classpath:car-patterns.json", "classpath:process-patterns.json"));

        indicators.setConfiguration(config);
        indicators.load();

        assertEquals(10, indicators.size());
    }

    @Test
    public void indiactorsEvaluationShallowCopy() {
        Indicator indicator = new Indicator();

        indicator.setName("Shallow copy test");
        indicator.setDescription("Shallow copy test description");

        IndicatorEvaluation evaluation = new IndicatorEvaluation(indicator, "data1=testdata1", "data2=testdata2", "data3=testdata3");

        assertEquals("Shallow copy test", evaluation.getName());
        assertEquals("Shallow copy test description", evaluation.getDescription());
        assertEquals("data1=testdata1; data2=testdata2; data3=testdata3", evaluation.getData());
    }
}
