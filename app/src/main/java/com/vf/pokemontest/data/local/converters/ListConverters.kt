package com.vf.pokemontest.data.local.converters

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class ListConverters {

    @TypeConverter
    fun stringListToJson(list: List<String>): String = adapter.toJson(list)

    @TypeConverter
    fun jsonToStringList(json: String): List<String> =
        adapter.fromJson(json) ?: emptyList()

    companion object {
        private val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        private val adapter = moshi.adapter<List<String>>(
            Types.newParameterizedType(List::class.java, String::class.java)
        )
    }
}