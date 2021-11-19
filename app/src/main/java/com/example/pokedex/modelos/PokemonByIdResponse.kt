package com.example.pokedex.modelos

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class PokemonByIdResponse: Serializable{

    @SerializedName("base_experience")
    private val baseExperience = 0
    private val name: String? = null
    private val id = 0
    private val height = 0
    private val weight = 0
    @SerializedName("sprites")
    private val sprites: Sprites? = null

    fun getBaseExperience(): Int {
        return baseExperience
    }

    fun getName(): String? {
        return name
    }

    fun getId(): Int {
        return id
    }

    fun getSprites(): Sprites?{
        return sprites
    }

    fun getHeight(): Int {
        return height
    }

    fun getWeight(): Int {
        return weight
    }
}