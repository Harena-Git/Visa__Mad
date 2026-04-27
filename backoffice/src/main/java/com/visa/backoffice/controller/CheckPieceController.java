package com.visa.backoffice.controller;

import com.visa.backoffice.entity.CheckPiece;
import com.visa.backoffice.entity.CheckPieceId;
import com.visa.backoffice.entity.Demandeur;
import com.visa.backoffice.service.CheckPieceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/check-pieces")
@CrossOrigin(origins = "*")
public class CheckPieceController {

    @Autowired
    private CheckPieceService checkPieceService;

    @GetMapping
    public ResponseEntity<List<CheckPiece>> getAllCheckPieces() {
        return ResponseEntity.ok(checkPieceService.findAll());
    }

    @PostMapping
    public ResponseEntity<CheckPiece> saveCheckPiece(@RequestBody CheckPiece checkPiece) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checkPieceService.save(checkPiece));
    }

    @GetMapping("/piece/{idPiece}")
    public ResponseEntity<List<CheckPiece>> getCheckPiecesByPiece(@PathVariable String idPiece) {
        return ResponseEntity.ok(checkPieceService.findByIdPiece(idPiece));
    }
}
