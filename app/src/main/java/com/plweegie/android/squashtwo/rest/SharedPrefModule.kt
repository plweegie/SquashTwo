package com.plweegie.android.squashtwo.rest


import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SharedPrefModule {

    @Provides
    @Singleton
    fun provideSharedPrefs(application: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(application)
}
