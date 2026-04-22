package visa.project.s6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import visa.project.s6.model.Demande;

public interface DemandeRepository extends JpaRepository<Demande, String> {
}