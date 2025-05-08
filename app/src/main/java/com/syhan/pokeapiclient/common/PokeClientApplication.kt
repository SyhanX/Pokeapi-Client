package com.syhan.pokeapiclient.common

import android.app.Application
import com.syhan.pokeapiclient.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PokeClientApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PokeClientApplication)
            modules(appModule)
        }
    }
}