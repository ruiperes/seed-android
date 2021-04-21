package io.ruiperes.seed.presentation.framework.archcomponents

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean


class HybridLiveEvent<T> : MutableLiveData<T>() {

    private val pending = AtomicBoolean(false)
    private val sticky = AtomicBoolean(false)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        super.observe(owner, Observer { t ->
            if (sticky.get() || pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        pending.set(true)
        super.setValue(t)
    }

    override fun postValue(value: T) {
        postValue(value, false)
    }

    fun postValue(t: T?, sticky: Boolean = false) {
        this.sticky.set(sticky)
        super.postValue(t)
    }
}
