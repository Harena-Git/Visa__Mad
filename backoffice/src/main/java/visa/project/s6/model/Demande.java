package visa.project.s6.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demande {
    @Id
    private String idDemande;

    private Date createdAt;
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    private CategorieDemande categorie;

    @ManyToOne
    @JoinColumn(name = "id_type_visa")
    private TypeVisa typeVisa;

    @ManyToOne
    @JoinColumn(name = "id_demandeur")
    private Demandeur demandeur;
}
