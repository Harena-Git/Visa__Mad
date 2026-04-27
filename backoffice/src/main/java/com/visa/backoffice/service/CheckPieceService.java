package com.visa.backoffice.service;

import com.visa.backoffice.entity.CheckPiece;
import com.visa.backoffice.entity.CheckPieceId;
import com.visa.backoffice.entity.Demandeur;
import com.visa.backoffice.repository.CheckPieceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CheckPieceService {

    @Autowired
    private CheckPieceRepository checkPieceRepository;

    public List<CheckPiece> findAll() { return checkPieceRepository.findAll(); }
    public Optional<CheckPiece> findById(CheckPieceId id) { return checkPieceRepository.findById(id); }
    public CheckPiece save(CheckPiece checkPiece) { return checkPieceRepository.save(checkPiece); }
    public void deleteById(CheckPieceId id) { checkPieceRepository.deleteById(id); }
    public boolean existsById(CheckPieceId id) { return checkPieceRepository.existsById(id); }

    public List<CheckPiece> findByIdPiece(String idPiece) {
        return checkPieceRepository.findByIdPiece(idPiece);
    }
    
    public List<CheckPiece> findByDemandeur(Demandeur demandeur) {
        return checkPieceRepository.findByDemandeur(demandeur);
    }
}
