package com.umc.biblioteca.repository;

import com.umc.biblioteca.model.Loan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LoanRepository extends MongoRepository<Loan, String> {
    List<Loan> findByReturnedFalse();
}
