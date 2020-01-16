package design.unstructured.examples.kafkafeedexample;

import java.util.Arrays;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}", "spring.cloud.stream.kafka.streams.binder.brokers=${spring.embedded.kafka.brokers}"},
		classes = {Main.class, KafkaAutoConfiguration.class})
@EmbeddedKafka(partitions = 1, topics = {"my-test-value", "my-test-value1"})
class KafkaFeedTest extends EmbeddedKafkaWrapper {

	@Test
	public void testSend() throws InterruptedException {
		Consumer<String, String> consumer = new KafkaFactoryProvider().consumerFactory(embeddedKafka).createConsumer();

		consumer.subscribe(Arrays.asList("my-test-value1"));

		for (int i = 0; i < 100; i++) {
			kafkaTemplate.send(new ProducerRecord<String, String>("my-test-value", "HOST-" + i, "testing one two three"));
			System.out.println("MAIN PRODUCED: " + KafkaTestUtils.getSingleRecord(consumer, "my-test-value1").value());
			Thread.sleep(1000);
		}
	}

	@Test
	void contextLoads() {
	}

}
