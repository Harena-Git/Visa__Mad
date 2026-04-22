package visa.project.s6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import visa.project.s6.model.Demandeur;

public interface DemandeurRepository extends JpaRepository<Demandeur, String> {
}