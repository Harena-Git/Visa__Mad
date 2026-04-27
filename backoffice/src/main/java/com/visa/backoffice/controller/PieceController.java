package com.visa.backoffice.controller;

import com.visa.backoffice.entity.Piece;
import com.visa.backoffice.service.PieceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pieces")
@CrossOrigin(origins = "*")
public class PieceController {

    @Autowired
    private PieceService pieceService;

    @GetMapping
    public ResponseEntity<List<Piece>> getAllPieces() {
        return ResponseEntity.ok(pieceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Piece> getPieceById(@PathVariable String id) {
        return pieceService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Piece> createPiece(@RequestBody Piece piece) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pieceService.save(piece));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Piece> updatePiece(@PathVariable String id, @RequestBody Piece piece) {
        if (!pieceService.existsById(id)) return ResponseEntity.notFound().build();
        piece.setId(id);
        return ResponseEntity.ok(pieceService.save(piece));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePiece(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        if (pieceService.existsById(id)) {
            pieceService.deleteById(id);
            response.put("message", "Pièce supprimée");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Non trouvé");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/type-visa/{idTypeVisa}")
    public ResponseEntity<List<Piece>> getPiecesByTypeVisa(@PathVariable String idTypeVisa) {
        return ResponseEntity.ok(pieceService.findByIdTypeVisa(idTypeVisa));
    }
}
