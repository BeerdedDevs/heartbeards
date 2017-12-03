package io.beerdeddevs.heartbeards.di.module

import android.app.Application
import com.google.firebase.analytics.FirebaseAnalytics
import com.vanniktech.rxpermission.RealRxPermission
import com.vanniktech.rxpermission.RxPermission
import dagger.Module
import dagger.Provides

@Module object ApplicationModule {
  @Provides
  @JvmStatic
  fun provideRxPermission(application: Application): RxPermission = RealRxPermission.getInstance(application)

  @Provides
  @JvmStatic
  fun provideFirebaseAnalytics(application: Application) = FirebaseAnalytics.getInstance(application)
}
