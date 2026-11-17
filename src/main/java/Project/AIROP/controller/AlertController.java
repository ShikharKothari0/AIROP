package Project.AIROP.controller;

import Project.AIROP.dto.AlertRequest;
import Project.AIROP.dto.AlertResponse;
import Project.AIROP.service.AlertService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public ResponseEntity<AlertResponse> createAlert(@Valid @RequestBody AlertRequest request){
        AlertResponse response= alertService.createAlert(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AlertResponse>> getAllAlerts(){
        List<AlertResponse> alerts= alertService.getAllAlerts();

        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertResponse> getAlertById(@PathVariable UUID id){
        AlertResponse response= alertService.getAlertById(id);
        return ResponseEntity.ok(response);

    }
}
