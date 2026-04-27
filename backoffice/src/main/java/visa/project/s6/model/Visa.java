package visa.project.s6.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visa {
    @Id
    private String idVisa;

    private String refVisa;
    private Date dateDebut;
    private Date dateFin;

    @ManyToOne
    @JoinColumn(name = "id_passport")
    private Passport passport;

    @ManyToOne
    @JoinColumn(name = "id_demande")
    private Demande demande;
}
