
package Project.AIROP.service;

import Project.AIROP.dto.AlertEvent;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AlertProducer {

    private static final Logger log =
            LoggerFactory.getLogger(AlertProducer.class);

    private static final String TOPIC = "alert-events";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public AlertProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<Void> publish(AlertEvent event) {
        final String message;

        try {
            message = objectMapper.writeValueAsString(event);
        } catch (JacksonException e) {
            log.error("Failed to serialize alert event: {}",
                    event.alertId(), e);
            return CompletableFuture.failedFuture(e);
        }

        return kafkaTemplate.send(
                        TOPIC,
                        event.alertId().toString(),
                        message)
                .thenAccept(result ->
                        log.info("Published alert event: {}",
                                event.alertId()))
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error(
                                "Failed to publish alert event: {}",
                                event.alertId(),
                                error);
                    }
                });
    }
}
