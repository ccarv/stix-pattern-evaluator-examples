package design.unstructured.examples.kafkafeedexample;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasKey;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;

import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducerTest;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}", "spring.cloud.stream.kafka.streams.binder.brokers=${spring.embedded.kafka.brokers}"},
		classes = {Main.class, KafkaAutoConfiguration.class}) // , KafkaFeedTest.TestConfig.class})
@EmbeddedKafka
class KafkaFeedTest {

	@Autowired
	EmbeddedKafkaBroker embeddedKafka;

	// @Configuration
	// public static class TestConfig {

	// // Adjust our standard topic properties to point metrics at our test topic
	// @Bean
	// public AppProperties appProperties() {
	// final AppProperties properties = new AppProperties();
	// properties.setMetrics(TOPIC_METRICS);
	// return properties;
	// }
	// }

	// @Test
	// public void testMetricsEncodedAsSent() {
	// ourService.send("tenant-1", KafkaMessageType.METRIC, "{\"id\":1}");

	// final Consumer<String, String> consumer = buildConsumer(StringDeserializer.class,
	// StringDeserializer.class);

	// embeddedKafka.consumeFromEmbeddedTopics(consumer, TOPIC_METRICS);
	// final ConsumerRecord<String, String> record = getSingleRecord(consumer, TOPIC_METRICS, 500);

	// // Use Hamcrest matchers provided by spring-kafka-test
	// // https://docs.spring.io/spring-kafka/docs/2.2.4.RELEASE/reference/#hamcrest-matchers
	// assertThat(record, hasKey("tenant-1"));
	// assertThat(record, hasValue("{\"id\":1}"));
	// }

	// private <K, V> Consumer<K, V> buildConsumer(Class<? extends Deserializer> keyDeserializer,
	// Class<? extends Deserializer> valueDeserializer) {
	// // Use the procedure documented at
	// // https://docs.spring.io/spring-kafka/docs/2.2.4.RELEASE/reference/#embedded-kafka-annotation

	// final Map<String, Object> consumerProps =
	// KafkaTestUtils.consumerProps("testMetricsEncodedAsSent", "true", embeddedKafka);
	// // Since we're pre-sending the messages to test for, we need to read from start
	// // of topic
	// consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	// // We need to match the ser/deser used in expected application config
	// consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer.getName());
	// consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer.getName());

	// final DefaultKafkaConsumerFactory<K, V> consumerFactory = new
	// DefaultKafkaConsumerFactory<>(consumerProps);
	// return consumerFactory.createConsumer();
	// }

	// @BeforeAll
	// void setup() {
	// Map<String, Object> configs = new HashMap<>(
	// KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker));

	// Consumer<String, String> consumer = new
	// DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(),
	// new StringDeserializer()).createConsumer();
	// consumer.subscribe(singleton(TOPIC));
	// }

	// private KafkaMessageListenerContainer<Integer, String> createContainer() {
	// Map<String, Object> props = consumerProps();
	// DefaultKafkaConsumerFactory<Integer, String> cf = new
	// DefaultKafkaConsumerFactory<Integer, String>(props);
	// KafkaMessageListenerContainer<Integer, String> container = new
	// KafkaMessageListenerContainer<>(cf, topic1);
	// return container;
	// }

	// private Map<String, Object> consumerProps() {
	// Map<String, Object> props = new HashMap<>();
	// props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	// props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
	// props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
	// props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
	// props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
	// props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
	// "org.apache.kafka.common.serialization.IntegerDeserializer");
	// props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
	// "org.apache.kafka.common.serialization.StringDeserializer");
	// return props;
	// }

	// private Map<String, Object> senderProps() {
	// Map<String, Object> props = new HashMap<>();
	// props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	// props.put(ProducerConfig.RETRIES_CONFIG, 0);
	// props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
	// props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
	// props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
	// props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
	// "org.apache.kafka.common.serialization.IntegerSerializer");
	// props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
	// "org.apache.kafka.common.serialization.StringSerializer");
	// return props;
	// }

	@Test
	void contextLoads() {
	}

}
