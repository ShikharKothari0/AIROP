package Project.AIROP.controller;
import Project.AIROP.dto.IncidentRequest;
import Project.AIROP.dto.IncidentResponse;
import Project.AIROP.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(@Valid @RequestBody IncidentRequest request) {
        IncidentResponse reponse = incidentService.createIncident(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(reponse);

    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getIncidentById(
            @PathVariable UUID id) {

        IncidentResponse response =
                incidentService.getIncidentById(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<IncidentResponse>> getAllIncidents(){
        List<IncidentResponse> incident= incidentService.getAllIncidents();
        return ResponseEntity.ok(incident);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponse> updateIncident(
            @PathVariable UUID id,
            @Valid @RequestBody IncidentRequest request) {

        IncidentResponse response =
                incidentService.updateIncident(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncident(
            @PathVariable UUID id) {

        incidentService.deleteIncident(id);

        return ResponseEntity.noContent().build();
    }

}
