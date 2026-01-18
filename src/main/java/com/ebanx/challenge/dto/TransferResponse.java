package com.ebanx.challenge.dto;

public record TransferResponse (
  AccountResponse origin,
  AccountResponse destination
) {}
