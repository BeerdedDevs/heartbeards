package io.beerdeddevs.heartbeards.di.component

import android.app.Application
import dagger.Component
import io.beerdeddevs.heartbeards.MainActivity
import io.beerdeddevs.heartbeards.di.module.ApplicationModule
import io.beerdeddevs.heartbeards.di.scope.ApplicationScope

@ApplicationScope
@Component(modules = arrayOf(ApplicationModule::class))
abstract class ApplicationComponent {

    companion object {
        fun create(application: Application): ApplicationComponent {
            return DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(application))
                    .build()
        }
    }

    abstract fun inject(activity: MainActivity)

}