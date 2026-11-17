package Project.AIROP.service;

import Project.AIROP.dto.OutboxMetricsResponse;
import Project.AIROP.repository.OutboxEventRepository;
import org.springframework.stereotype.Service;

@Service
public class OutboxMonitoringService {

    private final OutboxEventRepository outboxRepository;

    public OutboxMonitoringService(OutboxEventRepository outboxRepository){
        this.outboxRepository=outboxRepository;
    }

    public OutboxMetricsResponse getSummary(){
        return new OutboxMetricsResponse(
                outboxRepository.countByStatus("PENDING"),
                outboxRepository.countByStatus("PUBLISHED"),
                outboxRepository.countByStatus("DEAD")
        );
    }
}
