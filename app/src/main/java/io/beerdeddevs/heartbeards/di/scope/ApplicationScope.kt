package io.beerdeddevs.heartbeards.di.scope

import javax.inject.Scope

/** This scope is defined for the objects which need to live along the Activity lifecycle. */
@Scope
@MustBeDocumented
@Retention
annotation class ApplicationScope
