package com.example.crm.service;

import com.example.crm.exception.ResourceNotFoundException;
import com.example.crm.model.Seller;
import com.example.crm.repository.SellerRepository;
import com.example.crm.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final TransactionRepository transactionRepository;

    public SellerService(SellerRepository sellerRepository, TransactionRepository transactionRepository) {
        this.sellerRepository = sellerRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller createSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
    }

    public Seller updateSeller(Long id, Seller sellerDetails) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        seller.setName(sellerDetails.getName());
        seller.setContactInfo(sellerDetails.getContactInfo());
        return sellerRepository.save(seller);
    }

    @Transactional
    public void deleteSeller(Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));

        transactionRepository.deleteBySeller(seller);

        sellerRepository.deleteById(id);
    }

    public List<Seller> getTransactionsAmount(BigDecimal amount, String period) {
        LocalDateTime startDate = calculateStartDate(period);
        LocalDateTime endDate = LocalDateTime.now();

        return transactionRepository.findSellersWithTransactionAmountLessThan(startDate, endDate, amount);
    }

    public Optional<Seller> getTopSellerByPeriod(String period) {
        LocalDateTime startDate = calculateStartDate(period);
        LocalDateTime endDate = LocalDateTime.now();

        List<Seller> topSellers = transactionRepository.findTopSellerByPeriod(startDate, endDate);
        return topSellers.stream().findFirst();
    }

    private LocalDateTime calculateStartDate(String period) {
        LocalDateTime now = LocalDateTime.now();
        switch (period.toLowerCase()) {
            case "day":
                return now.minusDays(1);
            case "week":
                return now.minusWeeks(1);
            case "month":
                return now.minusMonths(1);
            case "year":
                return now.minusYears(1);
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
    }

    public com.example.crm.model.BestPeriod findBestTransactionPeriod(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
        List<com.example.crm.model.Transaction> transactions = transactionRepository.findBySeller(seller);

        Map<LocalDate, Integer> dailyCounts = new HashMap<>();
        Map<LocalDate, Integer> weeklyCounts = new HashMap<>();
        Map<LocalDate, Integer> monthlyCounts = new HashMap<>();

        for (com.example.crm.model.Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getTransactionDate().toLocalDate();
            dailyCounts.put(transactionDate, dailyCounts.getOrDefault(transactionDate, 0) + 1);
            LocalDate startOfWeek = transactionDate.with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek());
            weeklyCounts.put(startOfWeek, weeklyCounts.getOrDefault(startOfWeek, 0) + 1);
            LocalDate startOfMonth = transactionDate.withDayOfMonth(1);
            monthlyCounts.put(startOfMonth, monthlyCounts.getOrDefault(startOfMonth, 0) + 1);
        }

        LocalDate bestDay = null;
        int maxDailyTransactions = 0;

        for (Map.Entry<LocalDate, Integer> entry : dailyCounts.entrySet()) {
            if (entry.getValue() > maxDailyTransactions) {
                maxDailyTransactions = entry.getValue();
                bestDay = entry.getKey();
            }
        }

        LocalDate bestWeekStart = null;
        LocalDate bestWeekEnd = null;
        int maxWeeklyTransactions = 0;

        for (Map.Entry<LocalDate, Integer> entry : weeklyCounts.entrySet()) {
            if (entry.getValue() > maxWeeklyTransactions) {
                maxWeeklyTransactions = entry.getValue();
                bestWeekStart = entry.getKey();
                bestWeekEnd = bestWeekStart.plusDays(6);
            }
        }

        LocalDate bestMonthStart = null;
        LocalDate bestMonthEnd = null;
        int maxMonthlyTransactions = 0;

        for (Map.Entry<LocalDate, Integer> entry : monthlyCounts.entrySet()) {
            if (entry.getValue() > maxMonthlyTransactions) {
                maxMonthlyTransactions = entry.getValue();
                bestMonthStart = entry.getKey();
                bestMonthEnd = bestMonthStart.withDayOfMonth(bestMonthStart.lengthOfMonth());
            }
        }

        return new com.example.crm.model.BestPeriod(bestDay, maxDailyTransactions, bestWeekStart, bestWeekEnd, maxWeeklyTransactions, bestMonthStart, bestMonthEnd, maxMonthlyTransactions);
    }
}
