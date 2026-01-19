package com.ebanx.challenge.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class BalanceControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldReturnZeroForNonExistingAccount() throws Exception {
    mockMvc.perform(get("/balance")
        .param("account_id", "1234"))
      .andExpect(status().isNotFound())
      .andExpect(content().string("0"));
  }

  @Test
  void shouldReturnBalanceForExistingAccount() throws Exception {
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
      .andExpect(status().isCreated());

    mockMvc.perform(get("/balance")
        .param("account_id", "100"))
      .andExpect(status().isOk())
      .andExpect(content().string("10.0"));
  }
}
