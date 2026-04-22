package visa.project.s6.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statut {
    @Id
    private String idStatut;

    private String libelle;
}
