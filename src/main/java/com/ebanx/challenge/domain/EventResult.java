package com.ebanx.challenge.domain;

public class EventResult {

  private final String originId;
  private final Double originBalance;
  private final String destinationId;
  private final Double destinationBalance;

  // Deposit
  public EventResult(String destinationId, double destinationBalance) {
    this.originId = null;
    this.originBalance = null;
    this.destinationId = destinationId;
    this.destinationBalance = destinationBalance;
  }

  // Withdraw
  public EventResult(double originBalance, String originId) {
    this.originId = originId;
    this.originBalance = originBalance;
    this.destinationId = null;
    this.destinationBalance = null;
  }

  // Transfer
  public EventResult(
      String originId,
      double originBalance,
      String destinationId,
      double destinationBalance
  ) {
    this.originId = originId;
    this.originBalance = originBalance;
    this.destinationId = destinationId;
    this.destinationBalance = destinationBalance;
  }

  public String getOriginId() {
    return originId;
  }

  public Double getOriginBalance() {
    return originBalance;
  }

  public String getDestinationId() {
    return destinationId;
  }

  public Double getDestinationBalance() {
    return destinationBalance;
  }
}
