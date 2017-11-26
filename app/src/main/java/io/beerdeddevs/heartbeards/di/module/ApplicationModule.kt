package io.beerdeddevs.heartbeards.di.module

import android.app.Application
import com.vanniktech.rxpermission.RealRxPermission
import com.vanniktech.rxpermission.RxPermission
import dagger.Module
import dagger.Provides
import io.beerdeddevs.heartbeards.di.scope.ApplicationScope

@ApplicationScope
@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application = application

    @Provides fun provideRxPermission(): RxPermission = RealRxPermission.getInstance(application)
}
