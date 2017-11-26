package io.beerdeddevs.heartbeards.di.module

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.vanniktech.rxpermission.RealRxPermission
import com.vanniktech.rxpermission.RxPermission
import dagger.Module
import dagger.Provides
import io.beerdeddevs.heartbeards.di.scope.ApplicationScope
import io.beerdeddevs.heartbeards.preferences.BeardPrefs

@ApplicationScope
@Module
class ApplicationModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application = application

    @Provides
    fun provideRxPermission(): RxPermission = RealRxPermission.getInstance(application)

    @Provides
    fun provideFirebaseAnalytics(application: Application): FirebaseAnalytics = FirebaseAnalytics.getInstance(application)

    @Provides
    fun provideBeardPrefs(application: Application): BeardPrefs = BeardPrefs(application)
}
