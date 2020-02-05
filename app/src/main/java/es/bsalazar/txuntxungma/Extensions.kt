package es.bsalazar.txuntxungma

import android.arch.lifecycle.*

class NonNullMediatorLiveData<T> : MediatorLiveData<T>()

fun <T> MutableLiveData<T>.nonNull(): NonNullMediatorLiveData<T> {
    val mediator: NonNullMediatorLiveData<T> = NonNullMediatorLiveData()
    mediator.addSource(this) { it?.let { mediator.value = it } }
    return mediator
}

fun <T> NonNullMediatorLiveData<T>.observe(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(owner, android.arch.lifecycle.Observer {
        it?.let(observer)
    })
}

fun String.firstUpperCase() =
        when(length){
            0 -> this
            1 -> toUpperCase()
            else -> substring(0,1).toUpperCase() + substring(1)
        }