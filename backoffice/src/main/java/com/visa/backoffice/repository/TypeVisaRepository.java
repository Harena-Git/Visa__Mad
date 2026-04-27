package com.visa.backoffice.repository;

import com.visa.backoffice.entity.TypeVisa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeVisaRepository extends JpaRepository<TypeVisa, String> {
    List<TypeVisa> findByLibelleContainingIgnoreCase(String libelle);
}
