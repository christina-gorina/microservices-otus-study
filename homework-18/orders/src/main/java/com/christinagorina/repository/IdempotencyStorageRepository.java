package com.christinagorina.repository;


import com.christinagorina.model.IdempotencyStorage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IdempotencyStorageRepository extends JpaRepository<IdempotencyStorage, Long> {

    IdempotencyStorage findByIdempotencyKey(String idempotencyKey);

}
