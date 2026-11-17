package Project.AIROP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="alerts")
@Getter
@Setter
public class Alert {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String service;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private AlertSeverity severity;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
