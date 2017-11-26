package io.beerdeddevs.heartbeards.di.module

import android.app.Application
import dagger.Module
import dagger.Provides
import io.beerdeddevs.heartbeards.di.scope.ApplicationScope

@ApplicationScope
@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application = application

}