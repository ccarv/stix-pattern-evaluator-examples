package design.unstructured.examples.spe.example.kafkafeed;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import design.unstructured.examples.spe.example.objects.ProcessNode;
import design.unstructured.stix.evaluator.PatternEvaluator;
import design.unstructured.stix.evaluator.mapper.StixObservableMapper;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@EnableConfigurationProperties(KafkaFeedConfiguration.class)
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    @Autowired
    private StixObservableMapper mapper;

    @Autowired
    private Indicators indicators;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public Function<KStream<String, ProcessNode>, KStream<String, List<? extends Indicator>>> process() {
        return input -> input.map((host, process) -> this.analyze(host, process)).filter((host, evaluation) -> evaluation != null);
    }

    public KeyValue<String, List<? extends Indicator>> analyze(String host, ProcessNode process) {
        List<IndicatorEvaluation> detectedIndicators = new ArrayList<>();
        List<ProcessNode> activeProcessNodes = process.filter((node) -> node.getActiveEvents().containsKey("4688"));

        for (Indicator indicator : indicators) {
            if (indicator.getActive()) {
                for (ProcessNode analyzeNode : activeProcessNodes) {
                    try {
                        PatternEvaluator evaluator = new PatternEvaluator(indicator.getParsedPattern(), mapper, analyzeNode);

                        if (evaluator.get()) {
                            IndicatorEvaluation evaluation = new IndicatorEvaluation(indicator, "name=" + analyzeNode.getInfo().getName(),
                                    "command=" + analyzeNode.getInfo().getCommandLine());
                            detectedIndicators.add(evaluation);
                        }
                    } catch (Exception ex) {
                        logger.warn("failed trying to analyze indicator {}, disabling for now...", indicator.getName());
                        logger.warn("rule pattern: {}", indicator.getPattern());
                        indicator.setActive(false);
                    }
                }
            }
        }

        return new KeyValue<>(host, detectedIndicators.isEmpty() ? null : detectedIndicators);
    }
}
