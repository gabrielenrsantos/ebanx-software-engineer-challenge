package com.ebanx.challenge.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void reset() throws Exception {
    mockMvc.perform(post("/reset"))
      .andExpect(status().isOk());
  }

  @Test
  void shouldCreateAccountWithInitialDeposit() throws Exception {
    Map<String, Object> request = Map.of(
      "type", "DEPOSIT",
      "destination", "100",
      "amount", 10
    );

    String body = Objects.requireNonNull(
      objectMapper.writeValueAsString(request)
    );

    mockMvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.destination.id").value("100"))
      .andExpect(jsonPath("$.destination.balance").value(10.0));
  }

  @Test
  void shouldDepositIntoExistingAccount() throws Exception {
    Map<String, Object> deposit = Map.of(
      "type", "DEPOSIT",
      "destination", "100",
      "amount", 10
    );

    String body = Objects.requireNonNull(
      objectMapper.writeValueAsString(deposit)
    );

    mockMvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
      .andExpect(status().isCreated());

    mockMvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.destination.balance").value(20.0));
  }

  @Test
  void shouldFailWithdrawFromNonExistingAccount() throws Exception {
    Map<String, Object> request = Map.of(
      "type", "WITHDRAW",
      "origin", "200",
      "amount", 10
    );

    String body = Objects.requireNonNull(
      objectMapper.writeValueAsString(request)
    );

    mockMvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
      .andExpect(status().isNotFound())
      .andExpect(content().string("0"));
  }

  @Test
  void shouldWithdrawFromExistingAccount() throws Exception {
    Map<String, Object> deposit = Map.of(
      "type", "DEPOSIT",
      "destination", "100",
      "amount", 10
    );

    Map<String, Object> withdraw = Map.of(
      "type", "WITHDRAW",
      "origin", "100",
      "amount", 5
    );

    String depositBody = Objects.requireNonNull(
      objectMapper.writeValueAsString(deposit)
    );

    String withdrawBody = Objects.requireNonNull(
      objectMapper.writeValueAsString(withdraw)
    );

    mockMvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(depositBody))
      .andExpect(status().isCreated());

    mockMvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(withdrawBody))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.origin.id").value("100"))
      .andExpect(jsonPath("$.origin.balance").value(5.0));
  }

  @Test
  void shouldTransferBetweenAccounts() throws Exception {
    Map<String, Object> deposit = Map.of(
      "type", "DEPOSIT",
      "destination", "100",
      "amount", 15
    );

    Map<String, Object> transfer = Map.of(
      "type", "TRANSFER",
      "origin", "100",
      "destination", "300",
      "amount", 15
    );

    String depositBody = Objects.requireNonNull(
      objectMapper.writeValueAsString(deposit)
    );

    String transferBody = Objects.requireNonNull(
      objectMapper.writeValueAsString(transfer)
    );

    mockMvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(depositBody))
      .andExpect(status().isCreated());

    mockMvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(transferBody))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.origin.balance").value(0.0))
      .andExpect(jsonPath("$.destination.balance").value(15.0));
  }

  @Test
  void shouldFailTransferFromNonExistingAccount() throws Exception {
    Map<String, Object> request = Map.of(
      "type", "TRANSFER",
      "origin", "200",
      "destination", "300",
      "amount", 15
    );

    String body = Objects.requireNonNull(
      objectMapper.writeValueAsString(request)
    );

    mockMvc.perform(post("/event")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(body))
      .andExpect(status().isNotFound())
      .andExpect(content().string("0"));
  }
}
