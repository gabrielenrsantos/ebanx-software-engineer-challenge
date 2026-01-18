package com.ebanx.challenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ebanx.challenge.domain.Event;
import com.ebanx.challenge.dto.AccountResponse;
import com.ebanx.challenge.dto.DepositResponse;
import com.ebanx.challenge.dto.TransferResponse;
import com.ebanx.challenge.dto.WithdrawResponse;
import com.ebanx.challenge.service.LedgerService;

@RestController
public class EventController {

  private final LedgerService ledgerService;

  public EventController(LedgerService ledgerService) {
    this.ledgerService = ledgerService;
  }

  @PostMapping("/event")
  public ResponseEntity<Object> handleEvent(
    @RequestBody Event event
  ) {
    Object result = ledgerService.handleEvent(event);

    if (result == null) {
      return ResponseEntity.status(404).body(0);
    }

    Object[] data = (Object[]) result;

    return switch (event.type()) {
      case DEPOSIT -> {
        yield ResponseEntity.status(201).body(
          new DepositResponse(
            new AccountResponse(
              (String) data[1],
              (double) data[2]
            )
          )
        );
      }

      case WITHDRAW -> {
        yield ResponseEntity.status(201).body(
          new WithdrawResponse(
            new AccountResponse(
              (String) data[1],
              (double) data[2]
            )
          )
        );
      }

      case TRANSFER -> {
        yield ResponseEntity.status(201).body(
          new TransferResponse(
            new AccountResponse(
              (String) data[1],
              (double) data[2]
            ),
            new AccountResponse(
              (String) data[3],
              (double) data[4]
            )
          )
        );
      }
    };
  }
}
