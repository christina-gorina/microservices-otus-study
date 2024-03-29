package com.christinagorina.billing.repository;

import com.christinagorina.billing.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserName(String userName);
}