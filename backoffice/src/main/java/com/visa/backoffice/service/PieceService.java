package com.visa.backoffice.service;

import com.visa.backoffice.entity.Piece;
import com.visa.backoffice.repository.PieceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PieceService {

    @Autowired
    private PieceRepository pieceRepository;

    public List<Piece> findAll() { return pieceRepository.findAll(); }
    public Optional<Piece> findById(String id) { return pieceRepository.findById(id); }
    public Piece save(Piece piece) { return pieceRepository.save(piece); }
    public void deleteById(String id) { pieceRepository.deleteById(id); }
    public boolean existsById(String id) { return pieceRepository.existsById(id); }

    public List<Piece> findByIdTypeVisa(String idTypeVisa) {
        return pieceRepository.findByIdTypeVisa(idTypeVisa);
    }
}
