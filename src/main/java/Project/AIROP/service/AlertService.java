package Project.AIROP.service;

import Project.AIROP.dto.AlertEvent;
import Project.AIROP.entity.Alert;
import Project.AIROP.dto.AlertRequest;
import Project.AIROP.dto.AlertResponse;
import Project.AIROP.exception.AlertNotFoundException;
import Project.AIROP.repository.AlertRepository;
import Project.AIROP.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.databind.ObjectMapper;
import Project.AIROP.entity.OutboxEvent;
import tools.jackson.core.JacksonException;
import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;
    private final AlertProducer alertProducer;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public AlertResponse createAlert(AlertRequest request){
        Alert alert= new Alert();

        alert.setSource(request.getSource());
        alert.setService(request.getService());
        alert.setTitle(request.getTitle());
        alert.setDescription(request.getDescription());
        alert.setSeverity(request.getSeverity());
        alert.setCreatedAt(LocalDateTime.now());

        Alert savedAlert = alertRepository.save(alert);
        UUID eventId=UUID.randomUUID();
        AlertEvent event= new AlertEvent(
                eventId,
                savedAlert.getId(),
                savedAlert.getSource(),
                savedAlert.getService(),
                savedAlert.getTitle(),
                savedAlert.getDescription(),
                savedAlert.getSeverity(),
                savedAlert.getCreatedAt()
        );

        final String payload;

        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JacksonException e) {
            throw new IllegalStateException(
                    "Failed to serialize alert event", e);
        }

        OutboxEvent outboxEvent = new OutboxEvent(
                eventId,
                savedAlert.getId(),
                "alert-events",
                savedAlert.getId().toString(),
                payload
        );

//        outboxEvent.setId(eventId);
        outboxEventRepository.save(outboxEvent);
        return toResponse(savedAlert);

    }

    public List<AlertResponse> getAllAlerts(){
        return alertRepository.findAll().stream().map(this::toResponse).toList();
    }

    public AlertResponse getAlertById(UUID id){
        Alert alert = alertRepository.findById(id).orElseThrow(()-> new AlertNotFoundException("Alert not found with id: "+ id));
        return toResponse(alert);
    }

    private AlertResponse toResponse(Alert alert) {

        return new AlertResponse(
                alert.getId(),
                alert.getSource(),
                alert.getService(),
                alert.getTitle(),
                alert.getDescription(),
                alert.getSeverity(),
                alert.getCreatedAt()
        );
    }

}
