package design.unstructured.examples.spe.example.kafkafeed;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

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

    private static Long processedRecords = 0L;

    public static void main(String[] args) {

        Timer timer = new Timer("Timer");
        long delay = 5000L;
        long period = 5000L;

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (processedRecords != 0) {
                    logger.info("processed {} records in 5 seconds, {} eps...", processedRecords, processedRecords / 5);
                    processedRecords = 0L;
                }
            }
        }, delay, period);

        SpringApplication.run(Main.class, args);
    }

    @Bean
    public Function<KStream<String, ProcessNode>, KStream<String, List<IndicatorEvaluation>>> process() {
        return input -> input.mapValues(this::analyze).filter((host, evaluation) -> evaluation != null);
    }

    public List<IndicatorEvaluation> analyze(final String host, ProcessNode process) {
        processedRecords++;

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

        return detectedIndicators.isEmpty() ? null : detectedIndicators;
    }
}
