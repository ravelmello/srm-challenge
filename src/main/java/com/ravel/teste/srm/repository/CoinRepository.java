package com.ravel.teste.srm.repository;

import com.ravel.teste.srm.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Coin findByName(String name);
}
