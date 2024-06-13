package com.example.lab8.Repository;

import com.example.lab8.Entity.Pokemon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon,Integer> {
}
