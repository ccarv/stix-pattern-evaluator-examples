package design.unstructured.examples.spe.example.kafkafeed;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("kafka-feed")
public class KafkaFeedConfiguration {

    private final String stixObjectPackage;

    private final List<String> patterns;

    @ConstructorBinding
    public KafkaFeedConfiguration(String stixObjectPackage, List<String> patterns) {
        this.stixObjectPackage = stixObjectPackage;
        this.patterns = patterns;
    }

    public String getStixObjectPackage() {
        return stixObjectPackage;
    }

    public List<String> getPatterns() {
        return patterns;
    }
}
