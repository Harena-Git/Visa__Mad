package com.visa.backoffice.service;

import com.visa.backoffice.entity.TypeVisa;
import com.visa.backoffice.repository.TypeVisaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeVisaService {

    @Autowired
    private TypeVisaRepository typeVisaRepository;

    public List<TypeVisa> findAll() {
        return typeVisaRepository.findAll();
    }

    public Optional<TypeVisa> findById(String id) {
        return typeVisaRepository.findById(id);
    }

    public TypeVisa save(TypeVisa typeVisa) {
        return typeVisaRepository.save(typeVisa);
    }

    public void deleteById(String id) {
        typeVisaRepository.deleteById(id);
    }

    public List<TypeVisa> findByLibelleContaining(String libelle) {
        return typeVisaRepository.findByLibelleContainingIgnoreCase(libelle);
    }
}
