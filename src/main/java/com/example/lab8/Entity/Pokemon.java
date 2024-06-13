package com.example.lab8.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "pokemon")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpokemon", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "iduser", nullable = false)
    private User iduser;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "lugar", nullable = false, length = 100)
    private String lugar;

    @Column(name = "posibilidad", nullable = false)
    private Integer posibilidad;

}
