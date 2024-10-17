package com.example.crm.service;

import com.example.crm.exception.ResourceNotFoundException;
import com.example.crm.model.Seller;
import com.example.crm.model.Transaction;
import com.example.crm.repository.TransactionRepository;
import com.example.crm.repository.SellerRepository;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    public TransactionService(TransactionRepository transactionRepository, SellerRepository sellerRepository) {
        this.transactionRepository = transactionRepository;
        this.sellerRepository = sellerRepository;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    public List<Transaction> getTransactionsBySeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + sellerId));
        return transactionRepository.findBySeller(seller);
    }


}
