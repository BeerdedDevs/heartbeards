package io.beerdeddevs.heartbeards.di.component

import android.app.Application
import dagger.Component
import io.beerdeddevs.heartbeards.di.module.ApplicationModule
import io.beerdeddevs.heartbeards.di.scope.ApplicationScope
import io.beerdeddevs.heartbeards.feature.picture.choose.BottomSheetChoosePicture
import io.beerdeddevs.heartbeards.feature.signup.welcome.WelcomeActivity

@ApplicationScope
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    companion object {
        fun create(application: Application): ApplicationComponent {
            return DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(application))
                    .build()
        }
    }

    fun inject(activity: BottomSheetChoosePicture)

    fun inject(welcomeActivity: WelcomeActivity)
}
