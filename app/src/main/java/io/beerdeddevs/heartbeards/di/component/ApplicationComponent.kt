package io.beerdeddevs.heartbeards.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import io.beerdeddevs.heartbeards.HeartBeardsApplication
import io.beerdeddevs.heartbeards.di.module.ApplicationModule
import io.beerdeddevs.heartbeards.di.scope.ApplicationScope
import io.beerdeddevs.heartbeards.feature.picture.choose.BottomSheetChoosePicture
import io.beerdeddevs.heartbeards.feature.timeline.TimelineActivity

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
  fun inject(bottomSheet: BottomSheetChoosePicture)

  fun inject(activity: TimelineActivity)

  fun inject(application: HeartBeardsApplication)

  @Component.Builder interface Builder {
    @BindsInstance fun application(application: Application): Builder

    fun build(): ApplicationComponent
  }
}
