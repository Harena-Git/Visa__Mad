package visa.project.s6.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeVisa {
    @Id
    private String idTypeVisa;

    private String libelle;
}
