package Project.AIROP.dto;

import Project.AIROP.entity.AlertSeverity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AlertResponse {

    private UUID id;
    private String source;
    private String service;
    private String title;
    private String description;
    private AlertSeverity severity;
    private LocalDateTime createdAt;

}
