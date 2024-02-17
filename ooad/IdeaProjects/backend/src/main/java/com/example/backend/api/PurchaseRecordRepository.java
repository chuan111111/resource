package com.example.backend.api;

import com.example.backend.domain.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRecordRepository extends
        JpaRepository<PurchaseRecord, Long> {
}
