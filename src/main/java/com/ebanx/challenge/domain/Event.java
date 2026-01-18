package com.ebanx.challenge.domain;

public record Event(
  EventType type,
  String origin,
  String destination,
  double amount
) {}
