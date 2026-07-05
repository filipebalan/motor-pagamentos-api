package com.ingresso.pagamentos.repository;

import com.ingresso.pagamentos.domain.Conta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE) // A MÁGICA DA ALTA CONCORRÊNCIA AQUI
    @Query("SELECT c FROM Conta c WHERE c.idConta = :id")
    Optional<Conta> findByIdWithLock(String id);
}