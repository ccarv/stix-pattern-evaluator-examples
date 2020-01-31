package design.unstructured.examples.spe.example.kafkafeed;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ParallelServiceIntegrationTest
 */
@SpringBootTest(properties = {"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}"},
        classes = {Main.class, KafkaAutoConfiguration.class})
@EmbeddedKafka(partitions = 2, topics = {"process-objects-test", "process-matched-patterns-test"})
public class ParallelServiceIntegrationTest extends EmbeddedKafkaWrapper {

    @Test
    public void parallelServices_ReadFromPartitions() throws InterruptedException {
        System.out.println("Starting thread 2....");
        // new Thread(() -> {
        // SpringApplicationBuilder builder =
        // new
        // SpringApplicationBuilder(Main.class).properties("spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}");
        // builder.run("");
        // }).start();

        // System.out.println("Starting thread 2....");
        // new Thread(() -> {
        // SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        // builder.run("");
        // }).start();

        Thread.sleep(10000000);

    }
}
