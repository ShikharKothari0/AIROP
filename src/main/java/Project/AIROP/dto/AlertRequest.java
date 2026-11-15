package Project.AIROP.dto;

import Project.AIROP.entity.AlertSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertRequest {

    @NotBlank
    private String source;

    @NotBlank
    private String service;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull(message = "Severity is required")
    private AlertSeverity severity;
}
