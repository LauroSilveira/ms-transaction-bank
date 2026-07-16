package com.lauro.correia.transactionbank.infrastructure.adapters.out.repository.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuditRepository extends JpaRepository<Audit, UUID> {
}
