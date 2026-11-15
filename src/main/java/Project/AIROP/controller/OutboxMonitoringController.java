package Project.AIROP.controller;

import Project.AIROP.dto.OutboxMetricsResponse;
import Project.AIROP.service.OutboxMonitoringService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/monitoring/outbox")
public class OutboxMonitoringController {
    private final OutboxMonitoringService monitoringService;

    public OutboxMonitoringController(
            OutboxMonitoringService monitoringService
    ) {
        this.monitoringService = monitoringService;
    }

    @GetMapping
    public OutboxMetricsResponse getOutboxSummary() {
        return monitoringService.getSummary();
    }
}
