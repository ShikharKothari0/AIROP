package Project.AIROP.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class IncidentResponse {

    private UUID id;
    private String title;
    private String description;
    private String severity;
    private String status;
    private UUID createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
