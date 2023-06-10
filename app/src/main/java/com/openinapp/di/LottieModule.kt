package com.openinapp.di

import android.content.Context
import com.airbnb.lottie.LottieAnimationView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LottieModule {

    @Provides
    fun provideLottieAnimationView(@ApplicationContext context: Context): LottieAnimationView {
        return LottieAnimationView(context)
    }

}