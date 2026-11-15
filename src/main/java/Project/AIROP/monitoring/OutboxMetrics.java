package Project.AIROP.monitoring;
import Project.AIROP.repository.OutboxEventRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class OutboxMetrics {

    private final OutboxEventRepository outboxEventRepository;

    private final AtomicLong pendingEvents = new AtomicLong();
    private final AtomicLong publishedEvents = new AtomicLong();
    private final AtomicLong deadEvents = new AtomicLong();

    public OutboxMetrics(OutboxEventRepository outboxEventRepository, MeterRegistry meterRegistry){

        this.outboxEventRepository=outboxEventRepository;
        meterRegistry.gauge("airop.outbox.pending", pendingEvents);
        meterRegistry.gauge("airop.outbox.published", publishedEvents);
        meterRegistry.gauge("airop.outbox.dead",deadEvents);

    }

    @Scheduled(fixedDelay = 10000)
    public void refreshMetrics(){
        pendingEvents.set(outboxEventRepository.countByStatus("PENDING"));
        publishedEvents.set(
                outboxEventRepository.countByStatus("PUBLISHED")
        );

        deadEvents.set(
                outboxEventRepository.countByStatus("DEAD")
        );
    }
}
