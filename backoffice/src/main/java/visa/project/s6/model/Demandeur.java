package visa.project.s6.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Demandeur {
    @Id
    private String idDemandeur;

    private String nom;
    private String prenom;
    private String nomJeuneFille;
    private Date dtn;
    private String adresseMada;
    private String telephone;
    private String email;
    private Date createdAt;
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_nationalite")
    private Nationalite nationalite;

    @ManyToOne
    @JoinColumn(name = "id_situation_famille")
    private SituationFamille situationFamille;
}
