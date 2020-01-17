package design.unstructured.examples.speexamplekafkafeed;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;

/**
 * KafkaStreamsTest
 */
public abstract class EmbeddedKafkaWrapper {

    @Autowired
    EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @TestConfiguration
    public static class KafkaFactoryProvider {

        @Bean
        public ProducerFactory<String, String> producerFactory(@Autowired EmbeddedKafkaBroker embeddedKafka) {
            return new DefaultKafkaProducerFactory<>(new HashMap<>(KafkaTestUtils.producerProps(embeddedKafka)), new StringSerializer(),
                    new StringSerializer());
        }

        @Bean
        public ConsumerFactory<String, String> consumerFactory(@Autowired EmbeddedKafkaBroker embeddedKafka) {
            Map<String, Object> config = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafka));
            config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new StringDeserializer());
        }

        @Bean
        public KafkaTemplate<String, String> kafkaTemplate(@Autowired ProducerFactory<String, String> producerFactory) {
            KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory);
            return kafkaTemplate;
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafka.getPartitionsPerTopic());
        }
    }
}
