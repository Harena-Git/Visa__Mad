package visa.project.s6.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passport {
    @Id
    private String idPassport;

    private String numero;
    private Date delivreLe;
    private Date expireLe;

    @ManyToOne
    @JoinColumn(name = "id_demandeur")
    private Demandeur demandeur;
}
