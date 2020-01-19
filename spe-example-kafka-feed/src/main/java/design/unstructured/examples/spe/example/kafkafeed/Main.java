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
import design.unstructured.stix.evaluator.PatternEvaluatorException;
import design.unstructured.stix.evaluator.mapper.StixMapper;
import design.unstructured.stix.evaluator.mapper.StixMapperException;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
@EnableConfigurationProperties(ApplicationConfiguration.class)
public class Main {

	private static final Logger logger = LogManager.getLogger(Main.class);

	@Autowired
	private StixMapper mapper;

	@Autowired
	private Indicators indicators;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public Function<KStream<String, ProcessNode>, KStream<String, List<Indicator>>> process() {
		return input -> input.map((host, process) -> this.analyze(host, process)).filter((host, evaluation) -> evaluation != null);
	}

	public KeyValue<String, List<Indicator>> analyze(String host, ProcessNode process) {
		List<Indicator> detectedIndicators = new ArrayList<>();

		logger.info("evaluation [{}] for known threats on process: {}", host, process.getInfo().getName());

		for (Indicator indicator : indicators) {
			if (indicator.getActive()) {
				try {
					PatternEvaluator evaluator = new PatternEvaluator(indicator.getParsedPattern(), mapper, process);

					if (evaluator.get()) {
						logger.debug("-> Indicator detected! Name: {}", indicator.getName());
						detectedIndicators.add(indicator);
					}
				} catch (PatternEvaluatorException | StixMapperException ex) {
					ex.printStackTrace(System.out);
				}
			}
		}

		return new KeyValue<>(host, detectedIndicators.isEmpty() ? null : detectedIndicators);
	}
}
