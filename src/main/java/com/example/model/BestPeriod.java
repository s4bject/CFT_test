package com.example.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BestPeriod {
    private LocalDate bestDay;
    private int maxDailyTransactions;

    private LocalDate bestWeekStart;
    private LocalDate bestWeekEnd;
    private int maxWeeklyTransactions;

    private LocalDate bestMonthStart;
    private LocalDate bestMonthEnd;
    private int maxMonthlyTransactions;
}
