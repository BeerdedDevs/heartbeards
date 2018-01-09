package io.beerdeddevs.heartbeards.extension

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test

class RxExtensionsKtTest {
  @Test fun compositeDisposablePlusAssign() {
    val compositeDisposable = CompositeDisposable()
    assertThat(compositeDisposable.size()).isEqualTo(0)

    compositeDisposable += Disposables.empty()
    assertThat(compositeDisposable.size()).isEqualTo(1)
  }
}
