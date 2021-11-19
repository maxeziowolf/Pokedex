package com.example.pokedex


import retrofit2.http.GET
import retrofit2.http.Path
import com.example.pokedex.modelos.PokemonByIdResponse
import retrofit2.Call


interface PokeAPI {

    @GET("pokemon/{id}")
    fun getPokemonById(@Path("id") id: String?): Call<PokemonByIdResponse>
}