package Project.AIROP.service;

import Project.AIROP.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;
import Project.AIROP.dto.AlertEvent;
@Service
public class AlertConsumer {

    private static final Logger log= LoggerFactory.getLogger(AlertConsumer.class);
    private final ProcessedEventRepository processedEventRepository;
    private final ObjectMapper objectMapper;

    public AlertConsumer(ProcessedEventRepository processedEventRepository, ObjectMapper objectMapper){
        this.processedEventRepository=processedEventRepository;
        this.objectMapper=objectMapper;
    }

    @KafkaListener(topics="alert-events")
    @Transactional
    public void consume(String message) throws JacksonException{

        AlertEvent event = null;

        try {
            event = objectMapper.readValue(message, AlertEvent.class);

            int inserted =
                    processedEventRepository.insertIfNotExists(
                            event.eventId()
                    );

            if (inserted == 0) {
                log.warn(
                        "ALERT_EVENT_DUPLICATE eventId={} alertId={}",
                        event.eventId(),
                        event.alertId()
                );
                return;
            }

            log.info(
                    "ALERT_EVENT_PROCESSED eventId={} alertId={} severity={} service={}",
                    event.eventId(),
                    event.alertId(),
                    event.severity(),
                    event.service()
            );

        } catch (Exception e) {

            log.error(
                    "ALERT_EVENT_PROCESSING_FAILED eventId={} error={}",
                    event != null ? event.eventId() : "UNKNOWN",
                    e.getMessage(),
                    e
            );

            throw e;
        }
    }
}
