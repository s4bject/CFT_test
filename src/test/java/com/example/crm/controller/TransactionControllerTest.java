package com.example.crm.controller;

import com.example.crm.model.PaymentType;
import com.example.crm.model.Seller;
import com.example.crm.model.Transaction;
import com.example.crm.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private Transaction transaction;
    private Seller seller;

    @BeforeEach
    void setUp() {
        seller = new Seller();
        seller.setId(1L);
        seller.setName("Artem Timofeev");

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setSeller(seller);
        transaction.setAmount(BigDecimal.valueOf(1500));
        transaction.setPaymentType(PaymentType.CARD);
        transaction.setTransactionDate(LocalDateTime.now());
    }

    @Test
    void getAllTransactions_test() throws Exception {
        Mockito.when(transactionService.getAllTransactions()).thenReturn(Collections.singletonList(transaction));

        mockMvc.perform(get("/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].amount").value(1500))
                .andExpect(jsonPath("$[0].seller.name").value("Artem Timofeev"))
                .andExpect(jsonPath("$[0].paymentType").value("CARD"));
    }

    @Test
    void getTransactionById_test() throws Exception {
        Mockito.when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        mockMvc.perform(get("/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(1500))
                .andExpect(jsonPath("$.seller.name").value("Artem Timofeev"))
                .andExpect(jsonPath("$.paymentType").value("CARD"));
    }

    @Test
    void createTransaction_test() throws Exception {
        Mockito.when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":1500,\"transactionDate\":\"2024-10-17T10:00:00\",\"paymentType\":\"CARD\",\"seller\":{\"id\":1}}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(1500))
                .andExpect(jsonPath("$.seller.name").value("Artem Timofeev"))
                .andExpect(jsonPath("$.paymentType").value("CARD"));
    }

    @Test
    void getTransactionsBySeller_test() throws Exception {
        List<Transaction> transactions = Collections.singletonList(transaction);
        Mockito.when(transactionService.getTransactionsBySeller(1L)).thenReturn(transactions);

        mockMvc.perform(get("/transactions/seller/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].amount").value(1500))
                .andExpect(jsonPath("$[0].seller.name").value("Artem Timofeev"))
                .andExpect(jsonPath("$[0].paymentType").value("CARD"));
    }
}
