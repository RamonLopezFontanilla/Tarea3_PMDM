package com.example.tarea3.retrofit;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Método que decclara cada una de las consultas a la API
 */
public interface ApiService {

    @GET("pokemon?offset=0&limit=150")
    Call<PokemonRespuesta> obtenerListaPokemon();

    @GET("pokemon/{name}")
    Call<PokemonRespuestaDetalle> find(@Path("name") String name);

}
