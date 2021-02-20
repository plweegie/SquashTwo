package com.plweegie.android.squashtwo


import android.app.Application

import com.plweegie.android.squashtwo.data.RoomModule
import com.plweegie.android.squashtwo.rest.DaggerNetComponent
import com.plweegie.android.squashtwo.rest.NetComponent
import com.plweegie.android.squashtwo.rest.NetModule
import com.plweegie.android.squashtwo.rest.SharedPrefModule
import com.squareup.leakcanary.LeakCanary

class App : Application() {

    val netComponent: NetComponent by lazy {
        DaggerNetComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule(GITHUB_BASE_URL))
                .sharedPrefModule(SharedPrefModule())
                .roomModule(RoomModule(DATABASE_NAME))
                .build()
    }

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    companion object {
        private const val GITHUB_BASE_URL = "https://api.github.com/"
        private const val DATABASE_NAME = "repos"
    }
}
