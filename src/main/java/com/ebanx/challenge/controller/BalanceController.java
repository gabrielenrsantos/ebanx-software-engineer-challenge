package com.ebanx.challenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebanx.challenge.service.LedgerService;

@RestController
public class BalanceController {

  private final LedgerService ledgerService;

  public BalanceController(LedgerService ledgerService) {
    this.ledgerService = ledgerService;
  }

  @GetMapping("/balance")
  public ResponseEntity<Object> getBalance(
    @RequestParam("account_id") String accountId
  ) {
    if (!ledgerService.accountExists(accountId)) {
      return ResponseEntity.status(404).body(0);
    }

    double balance = ledgerService.getBalance(accountId);
    return ResponseEntity.ok(balance);
  }
}
