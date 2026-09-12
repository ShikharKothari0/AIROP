package Project.AIROP.dto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidentRequest {

    @NotBlank(message="Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message="Description is required")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    private String description;

    @NotNull
    @NotBlank(message = "Severity is required")
    private String severity;

    @NotNull
    @NotBlank(message = "Status is required")
    private String status;

}
