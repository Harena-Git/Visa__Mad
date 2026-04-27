package visa.project.s6.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieDemande {
    @Id
    private String idCategorie;

    private String libelle;
}
