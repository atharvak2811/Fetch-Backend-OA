package com.example.receiptservice.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.receiptservice.model.Receipt;

@Service
public class ReceiptService {

    private final Map<String, Receipt> receipts = new HashMap<>();
    private final Map<String, Integer> receiptPoints = new HashMap<>();

    public String processReceipt(Receipt receipt) {
        String id = UUID.randomUUID().toString();
        receipts.put(id, receipt);
        int points = calculatePoints(receipt);
        receiptPoints.put(id, points);
        return id;
    }

    public Integer getPoints(String id) {
        return receiptPoints.get(id);
    }

    private int calculatePoints(Receipt receipt) {
        int points = 0;

        // 1. Points for alphanumeric characters in retailer name
        points += receipt.getRetailer().replaceAll("[^a-zA-Z0-9]", "").length();

        // 2. Points for round dollar amount
        double total = Double.parseDouble(receipt.getTotal());
        if (total == Math.floor(total)) {
            points += 50;
        }

        // 3. Points if total is a multiple of 0.25
        if ((total * 100) % 25 == 0) {
            points += 25;
        }

        // 4. Points for every two items on the receipt
        points += (receipt.getItems().size() / 2) * 5;

        // 5. Points for items with description length multiple of 3
        for (var item : receipt.getItems()) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                double itemPrice = Double.parseDouble(item.getPrice());
                points += Math.ceil(itemPrice * 0.2);
            }
        }

        // 6. Points if purchase day is odd
        String[] dateParts = receipt.getPurchaseDate().split("-");
        int day = Integer.parseInt(dateParts[2]);
        if (day % 2 != 0) {
            points += 6;
        }

        // 7. Points if purchase time is between 2:00pm and 4:00pm
        String[] timeParts = receipt.getPurchaseTime().split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);
        if (hour == 14 || (hour == 15 && minute == 0)) {
            points += 10;
        }

        return points;
    }
}
