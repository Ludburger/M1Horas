package com.umc.biblioteca.repository;

import com.umc.biblioteca.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByStatus(String status);
    boolean existsByBookIdAndUserIdAndStatus(String bookId, String userId, String status);
}
