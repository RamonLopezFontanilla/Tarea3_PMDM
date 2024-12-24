package com.example.tarea3.retrofit;

/**
 * Clase que define la consulta a la API para obtener el listado de pokemons
 */

import com.example.tarea3.PokemonData;

import java.util.ArrayList;

public class PokemonRespuesta {
    private ArrayList<PokemonData> results;

    public ArrayList<PokemonData> getResults() {
        return results;
    }

    public void setResults(ArrayList<PokemonData> results) {
        this.results = results;
    }
}
