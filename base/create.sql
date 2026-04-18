CREATE TABLE type_visa(
   id_type_visa VARCHAR(50) ,
   libelle VARCHAR(150)  NOT NULL,
   PRIMARY KEY(id_type_visa)
);

CREATE TABLE situation_famille(
   id_situation_famille VARCHAR(50) ,
   libelle VARCHAR(150)  NOT NULL,
   PRIMARY KEY(id_situation_famille)
);

CREATE TABLE categorie_demande(
   id_categorie VARCHAR(50) ,
   libelle VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_categorie)
);

CREATE TABLE piece(
   id_piece VARCHAR(50) ,
   libelle VARCHAR(50)  NOT NULL,
   est_obligatoire INTEGER NOT NULL,
   id_type_visa VARCHAR(50) ,
   PRIMARY KEY(id_piece),
   FOREIGN KEY(id_type_visa) REFERENCES type_visa(id_type_visa)
);

CREATE TABLE nationalite(
   id_nationalite VARCHAR(50) ,
   libelle VARCHAR(150)  NOT NULL,
   PRIMARY KEY(id_nationalite)
);

CREATE TABLE statut(
   id_statut VARCHAR(50) ,
   libelle VARCHAR(150)  NOT NULL,
   PRIMARY KEY(id_statut)
);

CREATE TABLE champs(
   id_champs VARCHAR(50) ,
   libelle VARCHAR(150)  NOT NULL,
   est_obligatoire INTEGER NOT NULL,
   PRIMARY KEY(id_champs)
);

CREATE TABLE demandeur(
   id_demandeur VARCHAR(50) ,
   nom VARCHAR(250) ,
   prenom VARCHAR(250) ,
   nom_jeune_fille VARCHAR(50) ,
   dtn DATE,
   adresse_mada VARCHAR(250) ,
   telephone VARCHAR(50) ,
   email VARCHAR(50) ,
   created_at DATE,
   updated_at DATE,
   id_nationalite VARCHAR(50)  NOT NULL,
   id_situation_famille VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_demandeur),
   FOREIGN KEY(id_nationalite) REFERENCES nationalite(id_nationalite),
   FOREIGN KEY(id_situation_famille) REFERENCES situation_famille(id_situation_famille)
);

CREATE TABLE passport(
   id_passport VARCHAR(50) ,
   numero VARCHAR(150)  NOT NULL,
   delivre_le DATE NOT NULL,
   expire_le DATE NOT NULL,
   id_demandeur VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_passport),
   UNIQUE(numero),
   FOREIGN KEY(id_demandeur) REFERENCES demandeur(id_demandeur)
);

CREATE TABLE demande(
   id_demande VARCHAR(50) ,
   created_at DATE NOT NULL,
   updated_at DATE,
   id_demande_1 VARCHAR(50) ,
   id_categorie VARCHAR(50)  NOT NULL,
   id_type_visa VARCHAR(50)  NOT NULL,
   id_demandeur VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_demande),
   FOREIGN KEY(id_demande_1) REFERENCES demande(id_demande),
   FOREIGN KEY(id_categorie) REFERENCES categorie_demande(id_categorie),
   FOREIGN KEY(id_type_visa) REFERENCES type_visa(id_type_visa),
   FOREIGN KEY(id_demandeur) REFERENCES demandeur(id_demandeur)
);

CREATE TABLE carte_residence(
   id_carte_residence VARCHAR(50) ,
   ref_carte_residence VARCHAR(50) ,
   date_debut DATE NOT NULL,
   date_fin DATE NOT NULL,
   id_passport VARCHAR(50)  NOT NULL,
   id_demande VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_carte_residence),
   UNIQUE(ref_carte_residence),
   FOREIGN KEY(id_passport) REFERENCES passport(id_passport),
   FOREIGN KEY(id_demande) REFERENCES demande(id_demande)
);

CREATE TABLE visa_transformable(
   id_visa_transformable VARCHAR(50) ,
   ref_visa VARCHAR(50)  NOT NULL,
   date_debut DATE NOT NULL,
   date_fin DATE NOT NULL,
   id_passport VARCHAR(50)  NOT NULL,
   id_demandeur VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_visa_transformable),
   UNIQUE(ref_visa),
   FOREIGN KEY(id_passport) REFERENCES passport(id_passport),
   FOREIGN KEY(id_demandeur) REFERENCES demandeur(id_demandeur)
);

CREATE TABLE statut_demande(
   id_statut_demande VARCHAR(50) ,
   date_ DATE NOT NULL,
   id_statut VARCHAR(50) ,
   id_demande VARCHAR(50) ,
   PRIMARY KEY(id_statut_demande),
   FOREIGN KEY(id_statut) REFERENCES statut(id_statut),
   FOREIGN KEY(id_demande) REFERENCES demande(id_demande)
);

CREATE TABLE visa(
   id_visa VARCHAR(50) ,
   ref_visa VARCHAR(50)  NOT NULL,
   date_debut TIMESTAMP NOT NULL,
   date_fin TIMESTAMP NOT NULL,
   id_passport VARCHAR(50)  NOT NULL,
   id_demande VARCHAR(50)  NOT NULL,
   PRIMARY KEY(id_visa),
   UNIQUE(ref_visa),
   FOREIGN KEY(id_passport) REFERENCES passport(id_passport),
   FOREIGN KEY(id_demande) REFERENCES demande(id_demande)
);

CREATE TABLE check_piece(
   id_demande VARCHAR(50) ,
   id_piece VARCHAR(50) ,
   est_fourni BOOLEAN,
   updated_at DATE,
   PRIMARY KEY(id_demande, id_piece),
   FOREIGN KEY(id_demande) REFERENCES demande(id_demande),
   FOREIGN KEY(id_piece) REFERENCES piece(id_piece)
);
