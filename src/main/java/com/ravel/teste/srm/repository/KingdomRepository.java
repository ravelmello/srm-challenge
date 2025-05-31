package com.ravel.teste.srm.repository;

import com.ravel.teste.srm.entity.Kingdom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KingdomRepository extends JpaRepository<Kingdom, Integer> {
    Boolean existsByKingdomName(String kingdom);


    List<Kingdom> findAll();

    Kingdom getKingdomByKingdomName(String kingdomName);
}
