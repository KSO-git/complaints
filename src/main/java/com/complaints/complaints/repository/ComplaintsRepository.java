package com.complaints.complaints.repository;

import com.complaints.complaints.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComplaintsRepository extends JpaRepository<Complaint, Long> {
    Optional<Complaint> findByProductIdAndCustomer(String productId, String customer);
}
