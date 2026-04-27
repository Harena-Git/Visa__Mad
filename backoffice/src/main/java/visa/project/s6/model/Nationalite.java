package visa.project.s6.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Nationalite {
    @Id
    private String idNationalite;

    private String libelle;
}
