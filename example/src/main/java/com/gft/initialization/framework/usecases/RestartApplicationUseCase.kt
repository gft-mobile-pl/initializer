package com.gft.initialization.framework.usecases

import android.content.Context
import android.content.Intent
import kotlin.system.exitProcess

class RestartApplicationUseCase(
    private val applicationContext: Context,
) {
    operator fun invoke() {
        val packageLaunchIntent = applicationContext.packageManager.getLaunchIntentForPackage(applicationContext.packageName)!!
        applicationContext.startActivity(Intent.makeRestartActivityTask(packageLaunchIntent.component))
        exitProcess(0)
    }
}
