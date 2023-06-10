package com.openinapp.network

import android.app.Dialog

interface LoaderManager {
    fun showLoader()
    fun hideLoader()
}

class LoaderManagerImpl (private val dialog: Dialog) : LoaderManager {
    override fun showLoader() {
        if (!dialog.isShowing) dialog.show()
    }

    override fun hideLoader() {
        if (dialog.isShowing) dialog.dismiss()
    }
}

interface LoaderDialogHelper {
    fun performTaskWithLoader(task: () -> Unit)
}

class LoaderDialogHelperImpl(private val loaderManager: LoaderManager) : LoaderDialogHelper {
    override fun performTaskWithLoader(task: () -> Unit) {
        loaderManager.showLoader()
        try {
            task()
        } finally {
            loaderManager.hideLoader()
        }
    }
}