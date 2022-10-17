package com.hashedin.loginservice.repository;

import com.hashedin.loginservice.model.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {
}
