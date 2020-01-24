package design.unstructured.examples.spe.example.kafkafeed;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * IndicatorsTest
 */
// Load only the 'slice' required for this test, i.e. Indicators class in this case.
@SpringBootTest(classes = Indicators.class)

// Enables the support for the ApplicationConfiguration class ConfigurationProperties.
// Unfortunately, there are also unneeded beans in this class which also get loaded.
// You could separate those into other class (which won't be included in this test).
@EnableConfigurationProperties(ApplicationConfiguration.class)

// Use the test application.yml. @TestPropertySource loaded configuration has precedence over main application
// configuration.
@TestPropertySource("classpath:application.yml")
public class IndicatorsTest {

    // The previous String[] indicatorFiles is no longer relevant,
    // as the ApplicationConfiguration#indicatorFiles is now being picked up instead.


    @Autowired
    private Indicators indicators;

    @Test
    public void loadIndicatorJsonTest() {
        assertNotNull(indicators);
        assertEquals(5, indicators.size());
    }
}
