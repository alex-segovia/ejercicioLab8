package com.example.lab8.Controller;

import com.example.lab8.Dao.PokemonDao;
import com.example.lab8.Entity.Pokemon;
import com.example.lab8.Entity.User;
import com.example.lab8.Repository.PokemonRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class PokemonController {
    @Autowired
    PokemonDao pokemonDao;

    final PokemonRepository pokemonRepository;

    public PokemonController(PokemonRepository pokemonRepository){
        this.pokemonRepository = pokemonRepository;
    }

    @GetMapping("/pokemon/location")
    public Object getPokemonLocation(@RequestParam(value = "name",required = false) String name){
        if(name==null || name.isEmpty()){
            HashMap<String,Object> response = new HashMap<>();
            response.put("status","error");
            response.put("msg","Debe ingresar el nombre de un pokemon");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if(pokemonDao.obtenerLocationArea(name)==null || pokemonDao.obtenerLocationArea(name).isEmpty()){
            HashMap<String,Object> response = new HashMap<>();
            response.put("status","error");
            response.put("msg","El pokemon ingresado no existe");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return pokemonDao.obtenerLocationArea(name);
    }

    @GetMapping("/pokemon/method")
    public Object getEncounterMethod(@RequestParam(value = "name",required = false) String name){
        if(name==null || name.isEmpty()){
            HashMap<String,Object> response = new HashMap<>();
            response.put("status","error");
            response.put("msg","Debe ingresar un lugar de encuentro");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        if(pokemonDao.obtenerMetodoEncuentro(name)==null || pokemonDao.obtenerMetodoEncuentro(name).isEmpty()){
            HashMap<String,Object> response = new HashMap<>();
            response.put("status","error");
            response.put("msg","El lugar de encuentro ingresado no tiene resultados o no existe");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return pokemonDao.obtenerMetodoEncuentro(name);
    }

    @PostMapping("/pokemon")
    public ResponseEntity<HashMap<String,Object>> capturarPokemon(@RequestBody Pokemon pokemon){
        HashMap<String,Object> responseMap = new HashMap<>();
        if(pokemon.getNombre()==null || pokemon.getNombre().isEmpty()){
            responseMap.put("status","error");
            responseMap.put("msg","Debe ingresar el nombre del pokemon");
            return ResponseEntity.badRequest().body(responseMap);
        }
        String lugarElegido = "";
        int maxRate = 0;
        List<String> lugares = pokemonDao.obtenerLocationArea(pokemon.getNombre());
        if(lugares==null || lugares.isEmpty()){
            responseMap.put("status","error");
            responseMap.put("msg","El pokemon ingresado no existe");
            return ResponseEntity.badRequest().body(responseMap);
        }
        for(String l: lugares){
            if((int)pokemonDao.obtenerMetodoEncuentro(l).get("rate") > maxRate){
                maxRate = (int)pokemonDao.obtenerMetodoEncuentro(l).get("rate");
                lugarElegido = l;
            }
        }
        User user = new User();
        user.setId(1);
        pokemon.setIduser(user);
        pokemon.setLugar(lugarElegido);
        pokemon.setPosibilidad(maxRate);
        pokemonRepository.save(pokemon);
        responseMap.put("status","creado");
        HashMap<String,Object> pokemonCapturado = new HashMap<>();
        pokemonCapturado.put("nombre",pokemon.getNombre());
        pokemonCapturado.put("lugarDeEncuentro",lugarElegido);
        pokemonCapturado.put("posibilidadDeCaptura",maxRate);
        responseMap.put("pokemonCapturado",pokemonCapturado);
        return ResponseEntity.ok().body(responseMap);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String,String>> gestionExcepcion(HttpServletRequest request){
        HashMap<String,String> responseMap = new HashMap<>();
        if(request.getMethod().equals("POST")){
            responseMap.put("estado","error");
            responseMap.put("msg","Debe enviar un pokemon");
        }
        return ResponseEntity.badRequest().body(responseMap);
    }
}
