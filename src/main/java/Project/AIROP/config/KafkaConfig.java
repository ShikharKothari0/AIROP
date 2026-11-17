package Project.AIROP.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class KafkaConfig {
    private static final Logger log =
            LoggerFactory.getLogger(KafkaConfig.class);

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            KafkaTemplate<String, String> kafkaTemplate,
            MeterRegistry meterRegistry
    ) {

        Counter dltCounter = Counter.builder(
                        "airop.kafka.consumer.dlt.recoveries")
                .description("Total messages sent to the Dead Letter Topic")
                .register(meterRegistry);

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, exception) -> {

                            dltCounter.increment();

                            log.error(
                                    "KAFKA_DLT eventTopic={} dltTopic={} partition={} offset={} error={}",
                                    record.topic(),
                                    record.topic() + ".DLT",
                                    record.partition(),
                                    record.offset(),
                                    exception.getMessage(),
                                    exception
                            );

                            return new TopicPartition(
                                    record.topic() + ".DLT",
                                    0
                            );
                        }
                );

        FixedBackOff backOff = new FixedBackOff(2000L, 3L);

        return new DefaultErrorHandler(recoverer, backOff);
    }
    @Bean
    public NewTopic alertEventsDltTopic() {
        return TopicBuilder.name("alert-events.DLT")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
