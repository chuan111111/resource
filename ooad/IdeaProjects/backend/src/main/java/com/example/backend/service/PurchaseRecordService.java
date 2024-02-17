package com.example.backend.service;


import com.example.backend.domain.PurchaseRecord;

import java.util.List;

public interface PurchaseRecordService {
    public List<PurchaseRecord> findAll();
}