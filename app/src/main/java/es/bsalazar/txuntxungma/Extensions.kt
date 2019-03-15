package es.bsalazar.txuntxungma

import android.arch.lifecycle.*

open class Extensions {

    class NonNullMediatorLiveData<T> : MediatorLiveData<T>()

    open fun <T> MutableLiveData<T>.nonNull(): NonNullMediatorLiveData<T> {
        val mediator: NonNullMediatorLiveData<T> = NonNullMediatorLiveData()
        mediator.addSource(this, Observer { it?.let { mediator.value = it } })
        return mediator
    }

    open fun <T> NonNullMediatorLiveData<T>.observe(owner: LifecycleOwner, observer: (t: T) -> Unit) {
        this.observe(owner, android.arch.lifecycle.Observer {
            it?.let(observer)
        })
    }
}