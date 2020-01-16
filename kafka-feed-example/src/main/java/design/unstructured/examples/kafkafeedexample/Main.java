package design.unstructured.examples.kafkafeedexample;

import java.util.function.Function;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public Function<KStream<String, String>, KStream<String, String>> process() {
		return input -> input.map((key, value) -> new KeyValue<>(value, value)).map((key, value) -> new KeyValue<>(key, value.equals("testing one two three") ? "success!" : "failed"));
	}
}
