package Project.AIROP.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import Project.AIROP.entity.OutboxEvent;
import Project.AIROP.repository.OutboxEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import io.micrometer.core.instrument.Counter;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class OutboxPublisher {

    private static final Logger log= LoggerFactory.getLogger((OutboxPublisher.class));
    private final OutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final Counter publishAttempts;
    private final Counter publishFailures;
    private final Counter publishSuccesses;

    public OutboxPublisher(OutboxEventRepository outboxRepository,
                           KafkaTemplate<String, String> kafkaTemplate, MeterRegistry meterRegistry){
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.publishAttempts= Counter.builder("airop.outbox.publish.attempts").description("Total outbox publication attempts").register(meterRegistry);
        this.publishFailures = Counter.builder(
                        "airop.outbox.publish.failures")
                .description("Total failed outbox publication attempts")
                .register(meterRegistry);

        this.publishSuccesses = Counter.builder(
                        "airop.outbox.publish.successes")
                .description("Total successful outbox publications")
                .register(meterRegistry);
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents(){
        List<OutboxEvent> pendingEvents =
                outboxRepository
                        .findTop50ByStatusOrderByCreatedAtAsc("PENDING");

        for (OutboxEvent event : pendingEvents) {
            publishAttempts.increment();
            try {
                kafkaTemplate.send(
                        event.getTopic(),
                        event.getEventKey(),
                        event.getPayload()
                ).get(6, TimeUnit.SECONDS);

                event.markPublished();
                outboxRepository.save(event);
                publishSuccesses.increment();

                log.info("OUTBOX_PUBLISHED eventId={} topic={} aggregateId={} status={}",
                        event.getId(),
                        event.getTopic(),
                        event.getAggregateId(),
                        event.getStatus());

            } catch (Exception e) {
                publishFailures.increment();
                event.markFailed(e);
                outboxRepository.save(event);

                if ("DEAD".equals(event.getStatus())){
                    log.error("OUTBOX_DEAD eventId={} topic={} aggregateId={} attempts={} error={}",
                            event.getId(),
                            event.getTopic(),
                            event.getAggregateId(),
                            event.getAttempts(),
                            e.getMessage(),
                            e);
                }
                else{
                    log.warn("OUTBOX_RETRY eventId={} topic={} aggregateId={} attempts={} error={}",
                            event.getId(),
                            event.getTopic(),
                            event.getAggregateId(),
                            event.getAttempts(),
                            e.getMessage(),
                            e);
                }
            }
        }
    }
}
