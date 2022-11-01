package com.christinagorina.service;

import com.christinagorina.model.IdempotencyStorage;
import com.christinagorina.repository.IdempotencyStorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdempotencyStorageCleanService {

    private final IdempotencyStorageRepository idempotencyStorageRepository;

    @Scheduled(fixedDelay = 10000) // 10sec
    public void executeService() {
        log.info("IdempotencyStorageCleanService start...");
        List<IdempotencyStorage> idempotencyStorage = idempotencyStorageRepository.findAll();
        idempotencyStorage = idempotencyStorage.stream()
                .filter(i -> i.getCreationDateTime().plusSeconds(5).isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        idempotencyStorageRepository.deleteAll(idempotencyStorage);
        long count = idempotencyStorage.stream()
                .map(i -> {
                    idempotencyStorageRepository.deleteById(i.getId());
                    return true;
                }).count();
        log.info("Service delete + " + count + " old notes");
    }
}
