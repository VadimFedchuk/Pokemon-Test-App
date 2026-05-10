package com.vf.pokemontest.di

import android.app.Application
import android.content.Context
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.KoinTest
import org.koin.test.verify.definition
import org.koin.test.verify.injectedParameters
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class AppGraphTest : KoinTest {

    @Test
    fun `app graph resolves correctly`() {
        appModule.verify(
            extraTypes = listOf(Context::class, Application::class),
            injections = injectedParameters(
                definition<HttpLoggingInterceptor>(
                    HttpLoggingInterceptor.Logger::class
                )
            )
        )
    }
}