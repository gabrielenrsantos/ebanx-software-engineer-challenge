package com.ebanx.challenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ebanx.challenge.service.LedgerService;

@RestController
public class ResetController {

  private final LedgerService ledgerService;

  public ResetController(LedgerService ledgerService) {
    this.ledgerService = ledgerService;
  }

  @PostMapping("/reset")
  public ResponseEntity<Void> reset() {
    ledgerService.reset();
    return ResponseEntity.ok().build();
  }
}
