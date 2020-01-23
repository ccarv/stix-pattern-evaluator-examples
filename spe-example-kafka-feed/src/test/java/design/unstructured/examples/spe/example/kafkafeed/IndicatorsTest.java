package design.unstructured.examples.spe.example.kafkafeed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

/**
 * IndicatorsTest
 */
@SpringBootTest(classes = {Indicators.class})
public class IndicatorsTest {

    @Value("${kafka-feed.indicator-files}")
    public String[] indicatorFiles;

    @Autowired
    Indicators indicators;

    @Test
    public void loadIndicatorJsonTest() {
        assertNull(indicators);
        assertEquals(10, indicators.size());
    }
}
