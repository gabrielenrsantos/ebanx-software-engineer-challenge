package com.ebanx.challenge.domain;

import java.util.HashMap;
import java.util.Map;

public class Ledger {

  private final Map<String, Double> accounts = new HashMap<>();

  public void reset() {
    accounts.clear();
  }

  public boolean exists(String accountId) {
    return accounts.containsKey(accountId);
  }

  public double getBalance(String accountId) {
    return accounts.get(accountId);
  }

  public double deposit(String accountId, double amount) {
    double balance = accounts.getOrDefault(accountId, 0.0) + amount;
    accounts.put(accountId, balance);

    return balance;
  }

  public double withdraw(String accountId, double amount) {
    double balance = accounts.get(accountId) - amount;
    accounts.put(accountId, balance);

    return balance;
  }
}
