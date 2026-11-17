package Project.AIROP.service;

import Project.AIROP.entity.IncidentStatus;
import Project.AIROP.entity.Severity;
import Project.AIROP.dto.IncidentRequest;
import Project.AIROP.dto.IncidentResponse;
import Project.AIROP.entity.Incident;
import Project.AIROP.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import Project.AIROP.exception.IncidentNotFoundException;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class IncidentService {

    private final  IncidentRepository incidentRepository;
    public IncidentResponse createIncident(IncidentRequest request){
        Incident incident= new Incident();
        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setSeverity(Severity.valueOf(request.getSeverity().toUpperCase()));
        incident.setStatus(IncidentStatus.valueOf(request.getStatus().toUpperCase()));

        Incident savedIncident = incidentRepository.save(incident);

        return toResponse(savedIncident);
    }

    public IncidentResponse getIncidentById(UUID id) {

        Incident incident = findIncidentOrThrow(id);

        return toResponse(incident);
    }

    public List<IncidentResponse> getAllIncidents() {

        return incidentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public IncidentResponse updateIncident(
            UUID id,
            IncidentRequest request) {

        Incident existingIncident = findIncidentOrThrow(id);

        existingIncident.setTitle(request.getTitle());
        existingIncident.setDescription(request.getDescription());
        existingIncident.setSeverity(
                Severity.valueOf(request.getSeverity().toUpperCase())
        );

        existingIncident.setStatus(
                IncidentStatus.valueOf(request.getStatus().toUpperCase())
        );

        Incident updatedIncident =
                incidentRepository.save(existingIncident);

        return toResponse(updatedIncident);
    }

    public void deleteIncident(UUID id) {

        Incident existingIncident = findIncidentOrThrow(id);

        incidentRepository.delete(existingIncident);
    }

    private Incident findIncidentOrThrow(UUID id) {

        return incidentRepository.findById(id)
                .orElseThrow(() ->
                        new IncidentNotFoundException(
                                "Incident not found with id: " + id
                        ));
    }

    private IncidentResponse toResponse(Incident incident) {

        return new IncidentResponse(
                incident.getId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getSeverity().name(),
                incident.getStatus().name(),
                incident.getCreatedBy(),
                incident.getCreatedAt(),
                incident.getUpdatedAt()
        );
    }










}
