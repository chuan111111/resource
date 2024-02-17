package com.example.backend.service;

import com.example.backend.api.PurchaseRecordRepository;
import com.example.backend.domain.PurchaseRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseRecordServiceImpl implements PurchaseRecordService {
    @Autowired
    private PurchaseRecordRepository purchaseRecordRepository;

    @Override
    public List<PurchaseRecord> findAll() {
        return null;
    }
}