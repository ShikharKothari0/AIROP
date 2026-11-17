package Project.AIROP.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name="outbox_events")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class OutboxEvent {

    @Id
    private UUID id;

    @Column(name="aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(nullable = false)
    private String topic;

    @Column(name = "event_key", nullable = false)
    private String eventKey;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(nullable = false)
    private int attempts = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;

    public OutboxEvent(
            UUID eventId,
            UUID aggregateId,
            String topic,
            String eventKey,
            String payload
    ){
        this.id = eventId;
        this.aggregateId = aggregateId;
        this.topic = topic;
        this.eventKey = eventKey;
        this.payload = payload;
        this.status = "PENDING";
        this.attempts = 0;
        this.createdAt = LocalDateTime.now();
    }

    public void markPublished() {
        this.status = "PUBLISHED";
        this.publishedAt = LocalDateTime.now();
        this.lastError = null;
    }

    public void markFailed(Exception exception) {
        this.attempts++;
        this.lastError = exception.getClass().getSimpleName()
                + ": " + exception.getMessage();
        if (this.attempts>=5){
            this.status="DEAD";
        }
        else{
            this.status = "PENDING";
        }

    }

}
