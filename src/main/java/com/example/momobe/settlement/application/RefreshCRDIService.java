package com.example.momobe.settlement.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RefreshCRDIService {
    @Value("${banking.tran_id}")
    private String tranId;

    public String createCRDI() {
        Random rand = new Random();
        String randomNum = Integer.toString( rand.nextInt(8) + 1);
        for (int i = 0; i < 8; i++) {
            randomNum+= Integer.toString(rand.nextInt(9));
        }
        return tranId + "U" + randomNum;
    }
}
