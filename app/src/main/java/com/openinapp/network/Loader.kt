package com.openinapp.network

import android.app.Dialog
import android.content.Context
import com.airbnb.lottie.LottieAnimationView
import com.openinapp.R
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.annotation.VisibleForTesting
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object Loader {

    @Provides
    fun provideLoaderDialog(@ActivityContext context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.custom_loading)
        val lottie = dialog.findViewById<LottieAnimationView>(R.id.lottieView)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        return dialog;
    }

    @Provides
    fun provideLoaderManager(dialog: Dialog): LoaderManager {
        return LoaderManagerImpl(dialog)
    }

    @Provides
    fun provideLoaderDialogHelper(loaderManager: LoaderManager): LoaderDialogHelper {
        return LoaderDialogHelperImpl(loaderManager)
    }


}