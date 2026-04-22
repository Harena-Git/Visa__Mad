package visa.project.s6.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Champs {
    @Id
    private String idChamps;

    private String libelle;

    private Integer estObligatoire;
}
