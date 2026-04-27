package visa.project.s6.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SituationFamille {
    @Id
    private String idSituationFamille;

    private String libelle;
}
