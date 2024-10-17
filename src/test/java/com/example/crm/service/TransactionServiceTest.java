package com.example.crm.service;

import com.example.crm.exception.ResourceNotFoundException;
import com.example.crm.model.Transaction;
import com.example.crm.model.Seller;
import com.example.crm.repository.TransactionRepository;
import com.example.crm.repository.SellerRepository;
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

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTransactions_test() {
        List<Transaction> mockTransactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionRepository.findAll()).thenReturn(mockTransactions);

        List<Transaction> transactions = transactionService.getAllTransactions();

        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void createTransaction_test() {
        Transaction transaction = new Transaction();
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction savedTransaction = transactionService.createTransaction(transaction);

        assertNotNull(savedTransaction);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void getTransactionById_test() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));

        Transaction foundTransaction = transactionService.getTransactionById(1L);

        assertNotNull(foundTransaction);
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void getTransactionsBySeller_test() {
        Seller seller = new Seller();
        List<Transaction> mockTransactions = Arrays.asList(new Transaction(), new Transaction());
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(transactionRepository.findBySeller(seller)).thenReturn(mockTransactions);

        List<Transaction> transactions = transactionService.getTransactionsBySeller(1L);

        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findBySeller(seller);
    }

    @Test
    void getTransactionById_notFound_test() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getTransactionById(1L);
        });

        assertEquals("Transaction not found with id: 1", exception.getMessage());
        verify(transactionRepository, times(1)).findById(1L);
    }
}
