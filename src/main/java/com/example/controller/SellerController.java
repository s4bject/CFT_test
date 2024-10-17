package com.example.crm.controller;

import com.example.crm.model.Seller;
import com.example.crm.service.SellerService;
import com.example.crm.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    private final SellerService sellerService;
    private final TransactionService transactionService;

    public SellerController(SellerService sellerService, TransactionService transactionService) {
        this.sellerService = sellerService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Seller> getAllSellers() {
        return sellerService.getAllSellers();
    }

    @PostMapping
    public Seller createSeller(@RequestBody Seller seller) {
        return sellerService.createSeller(seller);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
        Seller seller = sellerService.getSellerById(id);
        return ResponseEntity.ok(seller);
    }

    @PutMapping("/{id}")
    public Seller updateSeller(@PathVariable Long id, @RequestBody Seller sellerDetails) {
        return sellerService.updateSeller(id, sellerDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{sellerId}/best-period")
    public ResponseEntity<com.example.crm.model.BestPeriod> getBestTransactionPeriod(@PathVariable Long sellerId) {
        com.example.crm.model.BestPeriod bestPeriod = sellerService.findBestTransactionPeriod(sellerId);
        return ResponseEntity.ok(bestPeriod);
    }

    @GetMapping("/top-seller/{period}")
    public ResponseEntity<Seller> getTopSeller(@PathVariable String period) {
        Optional<Seller> topSeller = sellerService.getTopSellerByPeriod(period);
        return topSeller.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/amount/{amount}/{period}")
    public ResponseEntity<List<Seller>> getTransactionsAmount(@PathVariable BigDecimal amount, @PathVariable String period) {
        List<Seller> sellers = sellerService.getTransactionsAmount(amount, period);
        return ResponseEntity.ok(sellers);
    }
}
