package Project.AIROP.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
