package com.ingresso.pagamentos;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ingresso.pagamentos.domain.Conta;
import com.ingresso.pagamentos.domain.Cpf;
import org.springframework.scheduling.annotation.EnableAsync;
import com.ingresso.pagamentos.repository.ContaRepository;

@SpringBootApplication
@EnableAsync // Permite que o Spring execute métodos em segundo plano
public class MotorPagamentosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MotorPagamentosApplication.class, args);
    }

    @Bean
    public CommandLineRunner testarBanco(ContaRepository repository) {
        return args -> {
            // 1. Inicialização do estado do banco de dados (Idempotência de teste)
            Conta contaFilipe = new Conta("ID-001", new Cpf("12345678909"), new BigDecimal("1000.00"));
            Conta contaEvento = new Conta("ID-002", new Cpf("98765432100"), new BigDecimal("0.00"));
            
            repository.save(contaFilipe);
            repository.save(contaEvento);
            
            System.out.println("Cofre aberto. Contas criadas no banco H2. Servidor Web aguardando requisições...");
        };
    }
}