package com.example.crm.controller;

import com.example.crm.model.Seller;
import com.example.crm.service.SellerService;
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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SellerController.class)
public class SellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SellerService sellerService;

    @MockBean
    private TransactionService transactionService;

    private Seller seller;

    @BeforeEach
    void setUp() {
        seller = new Seller();
        seller.setId(1L);
        seller.setName("Artem Timofeev");
        seller.setContactInfo("artyon20026@example.com");
        seller.setRegistrationDate(LocalDateTime.now());
    }

    @Test
    void getAllSellers_test() throws Exception {
        Mockito.when(sellerService.getAllSellers()).thenReturn(Collections.singletonList(seller));

        mockMvc.perform(get("/sellers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Artem Timofeev"));
    }

    @Test
    void getSellerById_test() throws Exception {
        Mockito.when(sellerService.getSellerById(1L)).thenReturn(seller);

        mockMvc.perform(get("/sellers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Artem Timofeev"));
    }

    @Test
    void createSeller_test() throws Exception {
        Mockito.when(sellerService.createSeller(any(Seller.class))).thenReturn(seller);

        mockMvc.perform(post("/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Artem Timofeev\",\"contactInfo\":\"artyon20026@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Artem Timofeev"));
    }

    @Test
    void updateSeller_test() throws Exception {
        Seller updatedSeller = new Seller();
        updatedSeller.setId(1L);
        updatedSeller.setName("Artem Timofeev");
        updatedSeller.setContactInfo("artyon20026@example.com");

        Mockito.when(sellerService.updateSeller(eq(1L), any(Seller.class))).thenReturn(updatedSeller);

        mockMvc.perform(put("/sellers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Artem Timofeev\",\"contactInfo\":\"artyon20026@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Artem Timofeev"));
    }

    @Test
    void deleteSeller_test1() throws Exception {
        mockMvc.perform(delete("/sellers/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(sellerService).deleteSeller(1L);
    }


    @Test
    void getTopSeller_test2() throws Exception {
        Mockito.when(sellerService.getTopSellerByPeriod("day")).thenReturn(Optional.of(seller));

        mockMvc.perform(get("/sellers/top-seller/day"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Artem Timofeev"));
    }

    @Test
    void getTransactionsAmount_test() throws Exception {
        Mockito.when(sellerService.getTransactionsAmount(BigDecimal.valueOf(1000), "day"))
                .thenReturn(Collections.singletonList(seller));

        mockMvc.perform(get("/sellers/amount/1000/day"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Artem Timofeev"));
    }
}
