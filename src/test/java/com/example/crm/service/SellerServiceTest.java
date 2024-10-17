package com.example.crm.service;

import com.example.crm.exception.ResourceNotFoundException;
import com.example.crm.model.Seller;
import com.example.crm.repository.SellerRepository;
import com.example.crm.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private SellerService sellerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSellers_test() {
        List<Seller> mockSellers = Arrays.asList(new Seller(), new Seller());
        when(sellerRepository.findAll()).thenReturn(mockSellers);

        List<Seller> sellers = sellerService.getAllSellers();

        assertEquals(2, sellers.size());
        verify(sellerRepository, times(1)).findAll();
    }

    @Test
    void createSeller_test() {
        Seller seller = new Seller();
        when(sellerRepository.save(seller)).thenReturn(seller);

        Seller savedSeller = sellerService.createSeller(seller);

        assertNotNull(savedSeller);
        verify(sellerRepository, times(1)).save(seller);
    }

    @Test
    void getSellerById_test1() {
        Seller seller = new Seller();
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        Seller foundSeller = sellerService.getSellerById(1L);

        assertNotNull(foundSeller);
        verify(sellerRepository, times(1)).findById(1L);
    }

    @Test
    void getSellerById_test2() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sellerService.getSellerById(1L));
    }

    @Test
    void updateSeller_test() {
        Seller existingSeller = new Seller();
        Seller updatedDetails = new Seller();
        updatedDetails.setName("Updated Name");

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(existingSeller));
        when(sellerRepository.save(existingSeller)).thenReturn(existingSeller);

        Seller updatedSeller = sellerService.updateSeller(1L, updatedDetails);

        assertEquals("Updated Name", updatedSeller.getName());
        verify(sellerRepository, times(1)).findById(1L);
        verify(sellerRepository, times(1)).save(existingSeller);
    }

    @Test
    void deleteSeller_test1() {
        Seller seller = new Seller();
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        sellerService.deleteSeller(1L);

        verify(transactionRepository, times(1)).deleteBySeller(seller);
        verify(sellerRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteSeller_test2() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> sellerService.deleteSeller(1L));
    }

    @Test
    void getTransactionsAmount_test() {
        BigDecimal amount = new BigDecimal("1000");
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now();
        List<Seller> mockSellers = Arrays.asList(new Seller(), new Seller());

        when(transactionRepository.findSellersWithTransactionAmountLessThan(startDate, endDate, amount))
                .thenReturn(mockSellers);

        List<Seller> sellers = sellerService.getTransactionsAmount(amount, "day");

        assertEquals(2, sellers.size());
        verify(transactionRepository, times(1)).findSellersWithTransactionAmountLessThan(any(), any(), eq(amount));
    }

    @Test
    void getTopSellerByPeriod_test1() {
        Seller seller = new Seller();
        when(transactionRepository.findTopSellerByPeriod(any(), any())).thenReturn(List.of(seller));

        Optional<Seller> topSeller = sellerService.getTopSellerByPeriod("day");

        assertTrue(topSeller.isPresent());
        verify(transactionRepository, times(1)).findTopSellerByPeriod(any(), any());
    }

    @Test
    void getTopSellerByPeriod_test2() {
        when(transactionRepository.findTopSellerByPeriod(any(), any())).thenReturn(List.of());

        Optional<Seller> topSeller = sellerService.getTopSellerByPeriod("day");

        assertTrue(topSeller.isEmpty());
    }
}
