package com.ifpr.leilao26.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ifpr.leilao26.model.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long>{
    
}
