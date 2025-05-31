package com.ravel.teste.srm.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Coin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    private Kingdom kingdom;

}
