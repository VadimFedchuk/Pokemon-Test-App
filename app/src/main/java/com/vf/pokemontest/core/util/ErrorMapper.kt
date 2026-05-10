package com.vf.pokemontest.core.util

import com.vf.pokemontest.R
import com.vf.pokemontest.core.ui.UiText
import retrofit2.HttpException
import java.io.IOException

class ErrorMapper {

    fun map(throwable: Throwable): UiText = when (throwable) {
        is IOException -> UiText.Resource(R.string.error_no_internet)
        is HttpException -> when (throwable.code()) {
            in 500..599 -> UiText.Resource(R.string.error_server_unavailable)
            else -> UiText.Resource(R.string.error_generic)
        }
        else -> UiText.Resource(R.string.error_generic)
    }
}