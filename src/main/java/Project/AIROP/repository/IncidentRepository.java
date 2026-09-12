package Project.AIROP.repository;

import Project.AIROP.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IncidentRepository extends JpaRepository<Incident, UUID> {

}