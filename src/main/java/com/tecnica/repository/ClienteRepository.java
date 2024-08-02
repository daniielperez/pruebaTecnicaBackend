package com.tecnica.repository;

import com.tecnica.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    public Optional<Cliente> findOneByShared(String shared);
    public Optional<Cliente> findByEmail(String email);
}
