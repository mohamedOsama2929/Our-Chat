package com.example.ososchat

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class ChatApp : MultiDexApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}