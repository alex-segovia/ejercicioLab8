package com.example.lab8.Dao;

import com.example.lab8.Entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class PokemonDao {
    public List<String> obtenerLocationArea(String name){
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<location_area_encounter[]> response = restTemplate.getForEntity("https://pokeapi.co/api/v2/pokemon/" + name + "/encounters", location_area_encounter[].class);
            List<String> location = new ArrayList<>();
            for (location_area_encounter l : response.getBody()) {
                location.add(l.getLocation_area().getName());
            }
            return location;
        }catch(HttpClientErrorException.NotFound e){
            return null;
        }
    }

    public HashMap<String,Object> obtenerMetodoEncuentro(String name){
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<location> response = restTemplate.getForEntity("https://pokeapi.co/api/v2/location-area/" + name + "/", location.class);
            int maxRate = 0;
            String nombre = "";
            for (encounter_method_rate encounter_method_rate : response.getBody().getEncounter_method_rates()) {
                for (version_details version_details : encounter_method_rate.getVersion_details()) {
                    if (version_details.getRate() > maxRate) {
                        maxRate = version_details.getRate();
                        nombre = encounter_method_rate.getEncounter_method().getName();
                    }
                }
            }
            HashMap<String, Object> metodo = new HashMap<>();
            metodo.put("nombre", nombre);
            metodo.put("rate", maxRate);
            return metodo;
        }catch (HttpClientErrorException.NotFound e){
            return null;
        }
    }
}
