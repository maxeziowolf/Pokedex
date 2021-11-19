package com.example.pokedex.modelos

import com.google.gson.annotations.SerializedName

class Sprites {

    private val back_default: String? = null
    private val back_female: String? = null
    private val back_shiny: String? = null
    private val back_shiny_female: String? = null
    private val front_default: String? = null
    private val front_female: String? = null
    private val front_shiny: String? = null
    private val front_shiny_female: String? = null
    @SerializedName("other")
    private val other: Other?=null

    fun getBackDefault(): String?{
        return back_default
    }

    fun getOther(): Other?{
        return other
    }

}
