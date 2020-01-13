package design.unstructured.examples.springkafkafeedexample;

import java.util.function.Consumer;
import java.util.function.Function;

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
	private Consumer<KStream<String, String>> consume() {
		return input -> input.foreach((key, value) -> {
			System.out.println("Key: " + key + " Value: " + value);
		});
	}

	@Bean
	public Function<KStream<String, String>, KStream<String, String>> consumeAndProduce() {

		return null; // input -> input.foreach((key,value) -> {});

	}

}
