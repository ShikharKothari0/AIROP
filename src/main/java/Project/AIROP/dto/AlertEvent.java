package Project.AIROP.dto;

import Project.AIROP.entity.AlertSeverity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record AlertEvent(UUID eventId,
                         UUID alertId,
                         String source,
                         String service,
                         String title,
                         String description,
                         AlertSeverity severity,
                         LocalDateTime createdAt) {
}
