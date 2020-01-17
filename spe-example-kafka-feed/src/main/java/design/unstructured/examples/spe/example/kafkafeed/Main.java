package design.unstructured.examples.spe.example.kafkafeed;

import java.util.function.Function;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import design.unstructured.examples.spe.example.ProcessNode;
import design.unstructured.examples.spe.example.Evaluation;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})

public class Main {

	private static final Logger logger = LogManager.getLogger(Main.class);

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public Function<KStream<String, ProcessNode>, KStream<String, Evaluation>> process() {
		return input -> input.map((host, process) -> this.analyze(host, process));
	}

	public KeyValue<String, Evaluation> analyze(String host, ProcessNode process) {
		logger.info("evaluation [{}] for known threats on process: {}" + process.getInfo().getName());

		return new KeyValue<>(host, new Evaluation());
	}

}
