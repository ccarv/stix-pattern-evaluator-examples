package design.unstructured.examples.spe.example.kafkafeed;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import design.unstructured.examples.spe.example.objects.ProcessNode;

@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"},
		classes = {Main.class, KafkaAutoConfiguration.class})
@EmbeddedKafka(partitions = 1, topics = {"process-objects-test", "process-matched-patterns-test"})
class MainKafkaTest extends EmbeddedKafkaWrapper {

	private static final Logger logger = LogManager.getLogger(MainKafkaTest.class);

	@Test
	public void kafkaCircuitTest() throws InterruptedException {
		Consumer<String, String> consumer = new KafkaFactoryProvider().consumerFactory(embeddedKafka).createConsumer();

		int receivedPongs = 0;

		consumer.subscribe(Arrays.asList("process-matched-patterns-test"));

		for (int i = 0; i < 3; i++) {
			kafkaTemplate.send(new ProducerRecord<String, String>("process-objects-test", "HOST-" + i,
					"{\"activeEvents\":{\"4688\":1,\"4656\":9,\"4690\":9,\"4663\":8}, \"info\": {\"name\": \"cmd.exe\",\"path\": \"C:\\\\Windows\\\\System32\",\"commandLine\": \"cmd.exe --noprofile\",\"id\": 598}}"));

			try {
				ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "process-matched-patterns-test", 1000);
				logger.info("Receiving ({}): {}", record.key(), record.value());
				receivedPongs++;
			} catch (IllegalStateException ex) {
				logger.warn("Pong was not received: getSingleRecord(...) timed out, no records consumed.");
			}
		}

		assertEquals(3, receivedPongs);
		consumer.close();
	}

	@Test
	public void sampleDatasetTest() throws IOException, InterruptedException {
		final Consumer<String, String> consumer = new KafkaFactoryProvider().consumerFactory(embeddedKafka).createConsumer();
		consumer.subscribe(Arrays.asList("process-matched-patterns-test"));

		ObjectMapper mapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		ObjectReader reader = mapper.readerFor(ProcessNode.class);
		Scanner scanner = new Scanner(MainKafkaTest.class.getResourceAsStream("/sample-dataset.json"));

		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		while (scanner.hasNextLine()) {
			String cluster = scanner.nextLine();
			ProcessNode node = reader.readValue(cluster);
			String jsonProcessNode = mapper.writeValueAsString(node);

			kafkaTemplate.send(new ProducerRecord<String, String>("process-objects-test", "HOSTTEST", jsonProcessNode));

			try {
				KafkaTestUtils.getRecords(consumer, 1000).forEach(record -> {
					try {
						Object json = mapper.readValue(record.value(), Object.class);
						logger.info("Receiving ({}): \n{}", record.key(), mapper.writeValueAsString(json));
					} catch (JsonProcessingException e) {
					}
				});

			} catch (IllegalStateException ex) {
				logger.warn("Pong was not received: getSingleRecord(...) timed out, no records consumed.");
			}
		}

		Thread.sleep(100000);

		scanner.close();
		consumer.close();
	}

}
