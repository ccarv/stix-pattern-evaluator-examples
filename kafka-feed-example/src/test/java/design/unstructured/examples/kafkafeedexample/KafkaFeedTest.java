package design.unstructured.examples.kafkafeedexample;

import java.util.Arrays;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@SpringBootTest(
		properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
				"spring.cloud.stream.kafka.streams.binder.brokers=${spring.embedded.kafka.brokers}"},
		classes = {Main.class, KafkaAutoConfiguration.class})
@EmbeddedKafka(partitions = 1, topics = {"kafka-feed-example-test", "my-test-value1"})
class KafkaFeedTest extends EmbeddedKafkaWrapper {

	@Test
	public void testSend() throws InterruptedException {
		Consumer<String, String> consumer = new KafkaFactoryProvider().consumerFactory(embeddedKafka).createConsumer();

		consumer.subscribe(Arrays.asList("process-matched-patterns-test"));

		for (int i = 0; i < 100; i++) {
			System.out.println("Sending (HOST-" + i + "): ping!");
			kafkaTemplate.send(new ProducerRecord<String, String>("process-objects-test", "HOST-" + i, "ping!"));

			ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "process-matched-patterns-test");
			System.out.println("Receiving (" + record.key() + "): " + record.value());
			Thread.sleep(1000);
		}
	}

}
