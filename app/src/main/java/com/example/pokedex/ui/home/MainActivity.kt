package com.example.pokedex.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit
import android.widget.Toast


import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.pokedex.PokeAPI
import com.example.pokedex.R
import com.example.pokedex.modelos.PokemonByIdResponse
import com.example.pokedex.ui.pokedexal.PokedexAleatorioActivity


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var  imagen_pokemon: ImageView
    private lateinit var  boton_ir_pokemon: Button
    private lateinit var  boton_salir_pokemon: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        supportActionBar?.hide()
        initComponentes()
        buscarPokemon((0..898).random())
    }

    fun initComponentes(){
        imagen_pokemon = findViewById(R.id.ivImagenPokemonInicio)
        boton_ir_pokemon = findViewById(R.id.bIrAPokedex)
        boton_salir_pokemon = findViewById(R.id.bSalir)

        boton_ir_pokemon?.setOnClickListener(this)
        boton_salir_pokemon?.setOnClickListener(this)

    }

    fun buscarPokemon(id: Int){
        val URL_BASE = "https://pokeapi.co/api/v2/"
        val retrofit = Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val pokemonAPI: PokeAPI = retrofit.create(PokeAPI::class.java)



        val call: Call<PokemonByIdResponse> = pokemonAPI.getPokemonById(id.toString())

        call.enqueue(object : Callback<PokemonByIdResponse?> {
            override fun onResponse(
                call: Call<PokemonByIdResponse?>?,
                response: Response<PokemonByIdResponse?>?
            ) {
                if (response!!.isSuccessful) {
                    val pokemon = response!!.body()!!
                    val EDteamImage = pokemon.getSprites()?.getOther()?.getDream_word()?.getFrontDefault()?.toString()

                    imagen_pokemon?.let { Glide.with(applicationContext).load(EDteamImage).into(it) }


                } else {
                    Log.e("error", "Hubo un error inesperado!")
                }
            }

            override fun onFailure(call: Call<PokemonByIdResponse?>?, t: Throwable?) {
                mostrarMensaje("Verifique su conexión a internet")
            }
        })


    }

    fun mostrarMensaje(mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.bIrAPokedex ->{
                var intent = Intent(this,PokedexAleatorioActivity::class.java)
                startActivity(intent)
            }

            R.id.bSalir ->{
                mostrarMensaje("Hasta luego")
                finishAffinity()
            }

        }
    }


}