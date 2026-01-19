package com.ebanx.challenge.service;

import org.springframework.stereotype.Service;

import com.ebanx.challenge.domain.Event;
import com.ebanx.challenge.domain.EventResult;
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

  public EventResult handleEvent(Event event) {
    try {
      return switch (event.type()) {
        case DEPOSIT -> {
          double balance =
              ledger.deposit(event.destination(), event.amount());

          yield new EventResult(event.destination(), balance);
        }

        case WITHDRAW -> {
          if (!ledger.exists(event.origin())) {
            yield null;
          }

          double balance =
              ledger.withdraw(event.origin(), event.amount());

          yield new EventResult(balance, event.origin());
        }

        case TRANSFER -> {
          if (!ledger.exists(event.origin())) {
            yield null;
          }

          double originBalance =
              ledger.withdraw(event.origin(), event.amount());
          double destinationBalance =
              ledger.deposit(event.destination(), event.amount());

          yield new EventResult(
              event.origin(),
              originBalance,
              event.destination(),
              destinationBalance
          );
        }
      };
    } catch (IllegalStateException e) {
      return null;
    }
  }
}
