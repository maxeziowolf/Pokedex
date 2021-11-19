package com.example.pokedex.modelos

import com.google.gson.annotations.SerializedName

class Other {
    @SerializedName("home")
    private val dream_worl: Home?=null

    fun getDream_word(): Home?{
        return dream_worl
    }
}
