package com.ebanx.challenge.service;

import org.springframework.stereotype.Service;

import com.ebanx.challenge.domain.Event;
import com.ebanx.challenge.domain.Ledger;

@Service
public class LedgerService {

  private final Ledger ledger = new Ledger();

  public void reset() {
    ledger.reset();
  }

  public boolean accountExists(String accountId) {
    return ledger.exists(accountId);
  }

  public double getBalance(String accountId) {
    return ledger.getBalance(accountId);
  }

  public Object handleEvent(Event event) {
    try {
      return switch (event.type()) {
        case DEPOSIT -> {
          double balance =
              ledger.deposit(event.destination(), event.amount());

          yield new Object[] { "destination", event.destination(), balance };
        }

        case WITHDRAW -> {
          if (!ledger.exists(event.origin())) {
            yield null;
          }

          double balance =
              ledger.withdraw(event.origin(), event.amount());

          yield new Object[] { "origin", event.origin(), balance };
        }

        case TRANSFER -> {
          if (!ledger.exists(event.origin())) {
            yield null;
          }

          double originBalance =
              ledger.withdraw(event.origin(), event.amount());
          double destinationBalance =
              ledger.deposit(event.destination(), event.amount());

          yield new Object[] {
            "transfer",
            event.origin(),
            originBalance,
            event.destination(),
            destinationBalance
          };
        }
      };
    } catch (IllegalStateException e) {
      return null;
    }
  }
}
