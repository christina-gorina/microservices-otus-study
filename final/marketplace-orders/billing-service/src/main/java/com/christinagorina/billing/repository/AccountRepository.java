package com.christinagorina.billing.repository;

import com.christinagorina.billing.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}