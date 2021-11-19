package com.example.pokedex.ui.pokedexal

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.pokedex.R
import android.os.CountDownTimer
import android.util.Log
import android.widget.*
import com.bumptech.glide.Glide
import com.example.pokedex.PokeAPI
import com.example.pokedex.modelos.PokemonByIdResponse
import com.example.pokedex.modelos.NetworkListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class PokedexAleatorioActivity : AppCompatActivity(), View.OnClickListener {
    var nombre_pokemon:TextView? = null
    var id_pokemon:TextView? = null
    var altura_pokemon:TextView? = null
    var peso_pokemon:TextView? = null
    var exp_base_pokemon:TextView? = null
    var imagen_pokemon:ImageView? = null
    var circulo:ImageView? = null
    var progresoTiempoBarra: ProgressBar? = null
    var boton_pokemon:Button? = null
    var boton_regresar_pokemon:Button? = null
    var  ban=true;
    var  conteo=0;
    var  cronometro: CountDownTimer? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokedex_aleatorio)

        //Configuracion inicial
        supportActionBar?.hide()
        initComponentes()



    }

    fun initComponentes(){
        nombre_pokemon = findViewById(R.id.tvNombrePokemon)
        id_pokemon = findViewById(R.id.tvIDPokemon)
        altura_pokemon = findViewById(R.id.tvAlturaPokemon)
        peso_pokemon = findViewById(R.id.tvPesoPokemon)
        exp_base_pokemon = findViewById(R.id.tvExpPokemon)
        imagen_pokemon = findViewById(R.id.ivImagenPokemon)
        circulo=findViewById(R.id.ivIndicador)
        progresoTiempoBarra = findViewById(R.id.pbTiempo)
        boton_pokemon = findViewById(R.id.bMostrarPokemon)
        boton_regresar_pokemon = findViewById(R.id.bRegresar)

        boton_pokemon?.setOnClickListener(this)
        boton_regresar_pokemon?.setOnClickListener(this)
    }

    fun buscarPokemon(id: Int){

        circulo?.let {it.setColorFilter(Color.YELLOW) }

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

                    circulo?.let {it.setColorFilter(Color.BLUE)}
                    nombre_pokemon?.setText(pokemon.getName().toString())
                    id_pokemon?.setText(pokemon.getId().toString())
                    altura_pokemon?.setText(pokemon.getHeight().toString()+" pies aprox")
                    peso_pokemon?.setText(pokemon.getWeight().toString()+" libras aprox")
                    exp_base_pokemon?.setText(pokemon.getBaseExperience().toString()+" aprox")
                    imagen_pokemon?.let { Glide.with(applicationContext).load(EDteamImage).into(it) }


                    mostrarMensaje("Se actualizo el Pokemón")

                    iniciarCronometro()
                } else {
                    Log.e("error", "Hubo un error inesperado!")
                }
            }

            override fun onFailure(call: Call<PokemonByIdResponse?>?, t: Throwable?) {
                mostrarMensaje("Verifique su conexión a internet")
            }
        })
    }

    fun iniciarCronometro(){
        progresoTiempoBarra?.progress=100
        cronometro =  object : CountDownTimer(30000, 300) {
            override fun onTick(p0: Long) {
                if (ban && conteo ==3){
                    ban=ban.not()
                    circulo?.let {it.setColorFilter(Color.RED)}
                    conteo=0
                }else if(conteo==3){
                    ban=ban.not()
                    circulo?.let {it.setColorFilter(Color.BLUE)}
                    conteo=0
                }else{
                    conteo=conteo.plus(1)
                }
                progresoTiempoBarra?.let { it.progress=it.progress.minus(1) }
            }

            override fun onFinish() {
                buscarPokemon((0..898).random())
            }
        }
        cronometro?.start()
    }

    fun mostrarMensaje(mensaje: String){
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.bMostrarPokemon->{
                cronometro?.cancel()
                buscarPokemon((0..898).random())
            }

            R.id.bRegresar->{
                mostrarMensaje("Hasta luego")
                finish()
            }

        }
    }

    private val broadcastReceiver by lazy {
        NetworkListener.create({
            cronometro?.let { it.cancel() }
            buscarPokemon((0..898).random())
        }, {
            mostrarMensaje("No tiene internet")
            cronometro?.let { it.cancel() }
        })
    }

    override fun onResume() {
        super.onResume()

        NetworkListener.register(this, broadcastReceiver)
    }

    override fun onPause() {
        super.onPause()
        cronometro?.let { it.cancel() }
        NetworkListener.unregister(this, broadcastReceiver)
    }

}