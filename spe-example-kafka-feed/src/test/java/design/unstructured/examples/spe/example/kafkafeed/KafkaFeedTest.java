package design.unstructured.examples.spe.example.kafkafeed;

import java.util.Arrays;
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

@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"},
		classes = {Main.class, KafkaAutoConfiguration.class})
@EmbeddedKafka(partitions = 1, topics = {"process-objects-test", "process-matched-patterns-test"})
class KafkaFeedTest extends EmbeddedKafkaWrapper {

	private static final Logger logger = LogManager.getLogger(KafkaFeedTest.class);

	@Test
	public void testSend() throws InterruptedException {
		Consumer<String, String> consumer = new KafkaFactoryProvider().consumerFactory(embeddedKafka).createConsumer();

		consumer.subscribe(Arrays.asList("process-matched-patterns-test"));

		for (int i = 0; i < 3; i++) {
			logger.info("Sending (HOST-{}): ping!", i);
			kafkaTemplate.send(new ProducerRecord<String, String>("process-objects-test", "HOST-" + i,
					"{\"info\":{\"name\":\"cmd1.exe\",\"path\":\"C:\\\\Windows\\\\System32\",\"commandLine\":\"cmd.exe --noprofile\",\"id\":598},\"children\":[{\"info\":{\"name\":\"ping.exe\",\"path\":\"C:\\\\Windows\\\\System32\",\"commandLine\":\"ping.exe google.com -t\",\"id\":587}},{\"info\":{\"name\":\"fdisk.exe\",\"path\":\"C:\\\\Windows\\\\System32\",\"commandLine\":\"fdisk.exe /format /reinstall\",\"id\":232}},{\"info\":{\"name\":\"nestat.exe\",\"path\":\"C:\\\\Windows\\\\System32\",\"commandLine\":\"netstat.exe --stuff\",\"id\":995}}]}"));

			try {
				ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "process-matched-patterns-test", 1000);
				logger.info("Receiving ({}): {}", record.key(), record.value());
			} catch (IllegalStateException ex) {
				logger.warn("Pong was not received: getSingleRecord(...) timed out, no records consumed.");
			}

			Thread.sleep(20000);
		}

		consumer.close();
	}

}
